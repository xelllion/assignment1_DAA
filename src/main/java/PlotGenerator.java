import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.*;

public class PlotGenerator {

    static class Record {
        String algo;
        int size;
        long timeNs;
        int maxDepth;

        Record(String algo, int size, long timeNs, int maxDepth) {
            this.algo = algo;
            this.size = size;
            this.timeNs = timeNs;
            this.maxDepth = maxDepth;
        }
    }

    public static void main(String[] args) {
        File dir = new File("plots");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        List<Record> records = new ArrayList<>();

        // Read results.csv
        try (BufferedReader br = new BufferedReader(new FileReader("results/results.csv"))) {
            String line = br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5 && parts[1].equals("Random")) {
                    records.add(new Record(parts[0], Integer.parseInt(parts[2]),
                            Long.parseLong(parts[3]), Integer.parseInt(parts[4])));
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading CSV: " + e.getMessage());
            return;
        }

        // Generate Plot 1: Time vs n
        generateSvg("plots/time_vs_n.svg", "Execution Time vs Input Size (n) - Random",
                "Size (n)", "Time (us)", records, r -> (double) r.timeNs / 1000.0);

        // Generate Plot 2: Recursion Depth vs n
        generateSvg("plots/depth_vs_n.svg", "Recursion Depth vs Input Size (n)",
                "Size (n)", "Max Recursion Depth", records, r -> (double) r.maxDepth);

        System.out.println("Plots successfully generated in plots/ folder (SVG format)!");
    }

    private static void generateSvg(String filePath, String title, String xLabel, String yLabel,
                                    List<Record> records, java.util.function.ToDoubleFunction<Record> valExtractor) {
        Map<String, List<Record>> byAlgo = new LinkedHashMap<>();
        for (Record r : records) {
            byAlgo.computeIfAbsent(r.algo, k -> new ArrayList<>()).add(r);
        }

        double maxX = 0;
        double maxY = 0;
        for (Record r : records) {
            if (r.size > maxX) maxX = r.size;
            double v = valExtractor.applyAsDouble(r);
            if (v > maxY) maxY = v;
        }
        if (maxY == 0) maxY = 1;

        int width = 800;
        int height = 500;
        int padLeft = 80;
        int padRight = 140;
        int padTop = 60;
        int padBottom = 60;

        int plotW = width - padLeft - padRight;
        int plotH = height - padTop - padBottom;

        String[] colors = {"#e6194B", "#3cb44b", "#ffe119", "#4363d8"};
        int cIdx = 0;

        try (PrintWriter out = new PrintWriter(new FileWriter(filePath))) {
            out.println("<svg xmlns='http://www.w3.org/2000/svg' width='" + width + "' height='" + height + "' style='background:#ffffff;'>");

            // Title
            out.println("<text x='" + (width / 2) + "' y='35' font-size='18' font-weight='bold' text-anchor='middle' font-family='sans-serif'>" + title + "</text>");

            // Axes
            out.println("<line x1='" + padLeft + "' y1='" + (padTop + plotH) + "' x2='" + (padLeft + plotW) + "' y2='" + (padTop + plotH) + "' stroke='#333' stroke-width='2'/>");
            out.println("<line x1='" + padLeft + "' y1='" + padTop + "' x2='" + padLeft + "' y2='" + (padTop + plotH) + "' stroke='#333' stroke-width='2'/>");

            // Labels
            out.println("<text x='" + (padLeft + plotW / 2) + "' y='" + (height - 15) + "' font-size='14' text-anchor='middle' font-family='sans-serif'>" + xLabel + "</text>");
            out.println("<text x='25' y='" + (padTop + plotH / 2) + "' font-size='14' text-anchor='middle' transform='rotate(-90 25 " + (padTop + plotH / 2) + ")' font-family='sans-serif'>" + yLabel + "</text>");

            // Lines and Legend
            int legendY = padTop + 20;
            for (Map.Entry<String, List<Record>> entry : byAlgo.entrySet()) {
                String color = colors[cIdx % colors.length];
                cIdx++;

                List<Record> list = entry.getValue();
                list.sort(Comparator.comparingInt(a -> a.size));

                StringBuilder pts = new StringBuilder();
                for (Record r : list) {
                    double x = padLeft + (r.size / maxX) * plotW;
                    double y = (padTop + plotH) - (valExtractor.applyAsDouble(r) / maxY) * plotH;
                    pts.append(String.format(Locale.US, "%.1f,%.1f ", x, y));
                    out.println(String.format(Locale.US, "<circle cx='%.1f' cy='%.1f' r='4' fill='%s'/>", x, y, color));
                }

                out.println("<polyline fill='none' stroke='" + color + "' stroke-width='3' points='" + pts.toString().trim() + "'/>");

                // Legend item
                out.println("<rect x='" + (width - padRight + 15) + "' y='" + (legendY - 10) + "' width='12' height='12' fill='" + color + "'/>");
                out.println("<text x='" + (width - padRight + 35) + "' y='" + legendY + "' font-size='12' font-family='sans-serif'>" + entry.getKey() + "</text>");
                legendY += 22;
            }

            out.println("</svg>");
        } catch (Exception e) {
            System.err.println("Error saving SVG: " + e.getMessage());
        }
    }
}
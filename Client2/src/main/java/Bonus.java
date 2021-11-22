import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Bonus {

    public static void main(String[] args) {
        List<PostRecord> records = new ArrayList<>();
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(
                    "results/output-thread-256.csv"));
            String line = reader.readLine();
            while (line != null) {
                // read next line
                line = reader.readLine();
                if (line == null) {break;}
                String[] parts = line.split(",");
                PostRecord postRecord = new PostRecord(Long.parseLong(parts[0]), Long.parseLong(parts[2]),
                        Integer.parseInt(parts[3]), parts[4]);
                records.add(postRecord);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<PostRecord> sortedRecords = records.stream()
                .sorted(Comparator.comparing(PostRecord::getStartTime))
                .collect(Collectors.toList());

        int latencys = 0;
        int start = 0;
        for (int i = 0; i < sortedRecords.size(); i++) {
            latencys += sortedRecords.get(i).getLatency();
            if (i > 0 && i % 1000 == 0 || i == sortedRecords.size() - 1) {
                try {
                    FileWriter fileWriter = new FileWriter("results/sorted-avg-threads-256.csv", true); //filename is e.g. /results/output-thread-32.csv
                    fileWriter.append(Math.round(latencys / (double)(i - start)) + "\n");
//                    fileWriter.append(sortedRecords.get(i).getLatency() + "\n");
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                start = i + 1;
                latencys = 0;
            }
        }
    }
}

import java.util.*;

public class OptimalPageReplacement {

    // Function to implement Optimal Page Replacement Algorithm
    public static void optimalPage(int[] pg, int pn, int fn) throws InterruptedException {
        
        // Create physical frames array and initialize as empty (-1)
        int[] fr = new int[fn];
        Arrays.fill(fr, -1);
        
        int hit = 0;
        int miss = 0;

        System.out.println("==========================================================================");
        System.out.println("            OPTIMAL PAGE REPLACEMENT ALGORITHM SIMULATION                 ");
        System.out.println("==========================================================================");
        System.out.printf("Total Memory Frames: %d | Total Page Requests: %d\n\n", fn, pn);

        // Process each page reference one by one
        for (int i = 0; i < pn; i++) {
            int currentPage = pg[i];
            boolean isHit = false;

            // 1. Check for PAGE HIT
            for (int j = 0; j < fn; j++) {
                if (fr[j] == currentPage) {
                    hit++;
                    isHit = true;
                    break;
                }
            }

            // 2. Handle PAGE MISS / FAULT if not in memory
            if (!isHit) {
                miss++;
                boolean emptyFrameFound = false;

                // 2a. Check if there is an available empty frame slot
                for (int j = 0; j < fn; j++) {
                    if (fr[j] == -1) {
                        fr[j] = currentPage;
                        emptyFrameFound = true;
                        break;
                    }
                }

                // 2b. If memory is full, perform OPTIMAL LOOK-AHEAD eviction
                if (!emptyFrameFound) {
                    int farthest = -1;
                    int replaceIndex = -1;

                    for (int j = 0; j < fn; j++) {
                        int k;
                        // Scan forward in reference string to find next access time
                        for (k = i + 1; k < pn; k++) {
                            if (fr[j] == pg[k]) {
                                if (k > farthest) {
                                    farthest = k;
                                    replaceIndex = j;
                                }
                                break;
                            }
                        }
                        // If a resident page is never referenced again, evict it immediately
                        if (k == pn) {
                            replaceIndex = j;
                            break;
                        }
                    }
                    fr[replaceIndex] = currentPage;
                }
            }

            // --- DISPLAY CURRENT TIMED STEP OUTPUT ---
            System.out.printf("⏱ Step %02d [Requesting Page %d] ➔ Status: %s\n", 
                              (i + 1), currentPage, (isHit ? "PAGE HIT" : "PAGE MISS / FAULT"));
            
            System.out.print("   RAM Frames State: [ ");
            for (int j = 0; j < fn; j++) {
                if (fr[j] == -1) System.out.print("- ");
                else System.out.print(fr[j] + " ");
            }
            System.out.println("]\n");

            // Timed Execution Delay (e.g., 1.5 seconds delay between steps)
            Thread.sleep(1500); 
        }

        // --- FINAL METRICS SUMMARY ---
        System.out.println("==========================================================================");
        System.out.println("                        SIMULATION RUN SUMMARY                            ");
        System.out.println("==========================================================================");
        System.out.println("Total Page Requests: " + pn);
        System.out.printf("Total Page Hits    : %d (%.1f%%)\n", hit, ((double) hit / pn) * 100);
        System.out.printf("Total Page Faults  : %d (%.1f%%)\n", miss, ((double) miss / pn) * 100);
        System.out.println("==========================================================================");
    }

    // Driver Code
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);

            System.out.print("Enter number of memory frames: ");
            int fn = scanner.nextInt();

            System.out.print("Enter number of page requests: ");
            int pn = scanner.nextInt();

            int[] pg = new int[pn];
            System.out.println("Enter page reference string (space-separated): ");
            for (int i = 0; i < pn; i++) {
                pg[i] = scanner.nextInt();
            }

            optimalPage(pg, pg.length, fn);

        } catch (InterruptedException e) {
            System.out.println("Simulation interrupted.");
        }
    }
}
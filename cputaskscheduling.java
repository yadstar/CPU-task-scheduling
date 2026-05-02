package hahah;

import java.util.*;

class Process {
    int id, at, bt, rt, ct, wt, tat;

    Process(int id, int at, int bt) {
        this.id = id;
        this.at = at;
        this.bt = bt;
        this.rt = bt;
    }
}
public class cputaskscheduling {

    static List<Process> copy(List<Process> list) {
        List<Process> c = new ArrayList<>();
        for (Process p : list)
            c.add(new Process(p.id, p.at, p.bt));
        return c;
    }
    // ================= FCFS =================
    static void fcfs(List<Process> p) {
        p.sort(Comparator.comparingInt(x -> x.at));
        int time = 0;

        for (Process x : p) {
            if (time < x.at) time = x.at;

            x.wt = time - x.at;
            time += x.bt;
            x.ct = time;
            x.tat = x.ct - x.at;
        }
        print("FCFS", p);
    }
    // ================= SJF (Non-preemptive) =================
    static void sjf(List<Process> p) {
        int n = p.size();
        boolean[] done = new boolean[n];
        int time = 0, completed = 0;
        List<Process> order = new ArrayList<>();

        while (completed < n) {
            int idx = -1;
            int min = Integer.MAX_VALUE;

            for (int i = 0; i < n; i++) {
                if (!done[i] && p.get(i).at <= time && p.get(i).bt < min) {
                    min = p.get(i).bt;
                    idx = i;
                }
            }
            if (idx == -1) {
                time++;
                continue;
            }
            Process x = p.get(idx);

            x.wt = time - x.at;
            time += x.bt;
            x.ct = time;
            x.tat = x.ct - x.at;

            done[idx] = true;
            order.add(x);
            completed++;
        }
        print("SJF", order);
    }
    // ================= Round Robin =================
    static void rr(List<Process> p, int q) {
        Queue<Process> q1 = new LinkedList<>();
        p.sort(Comparator.comparingInt(x -> x.at));

        int time = 0, i = 0, done = 0;
        int n = p.size();

        while (done < n) {

            while (i < n && p.get(i).at <= time)
                q1.add(p.get(i++));

            if (q1.isEmpty()) {
                time++;
                continue;
            }
            Process x = q1.poll();

            int run = Math.min(q, x.rt);
            x.rt -= run;
            time += run;

            while (i < n && p.get(i).at <= time)
                q1.add(p.get(i++));
            
            if (x.rt > 0) {
                q1.add(x);
            } else {
                x.ct = time;
                x.tat = x.ct - x.at;
                x.wt = x.tat - x.bt;
                done++;
            }
        }
        print("Round Robin", p);
    }
    // ================= PRINT =================
    static void print(String name, List<Process> p) {
        System.out.println("\n--- " + name + " ---");
        double wt = 0, tat = 0;

        System.out.println("ID AT BT WT TAT");

        for (Process x : p) {
            wt += x.wt;
            tat += x.tat;
            System.out.println(x.id + "  " + x.at + "  " + x.bt + "  " + x.wt + "  " + x.tat);
        }
        System.out.println("Avg WT = " + wt / p.size());
        System.out.println("Avg TAT = " + tat / p.size());
    }
    // ================= MAIN =================
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Number of processes: ");
        int n = sc.nextInt();

        List<Process> p = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            System.out.print("AT BT: ");
            int at = sc.nextInt();
            int bt = sc.nextInt();
            p.add(new Process(i + 1, at, bt));
        }
        fcfs(copy(p));
        sjf(copy(p));

        System.out.print("Quantum: ");
        int q = sc.nextInt();

        rr(copy(p), q);

        sc.close();
    }
}
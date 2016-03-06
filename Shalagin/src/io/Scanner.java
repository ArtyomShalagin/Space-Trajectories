package io;

import java.io.*;
import java.util.StringTokenizer;

public class Scanner {
    private BufferedReader br;
    private StringTokenizer st;

    public Scanner() throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        st = new StringTokenizer(br.readLine());
    }

    public Scanner(File source) throws IOException {
        br = new BufferedReader(new FileReader(source));
        st = new StringTokenizer(br.readLine());
    }

    public int nextInt() throws IOException {
        return Integer.parseInt(next());
    }

    public long nextLong() throws IOException {
        return Long.parseLong(next());
    }

    public double nextDouble() throws IOException {
        return Double.parseDouble(next());
    }

    public boolean nextBoolean() throws IOException{
        return next().equals("true");
    }

    public String next() throws IOException {
        while (!st.hasMoreTokens()) {
            String s = br.readLine();
            if (s == null)
                throw new IOException("No more Strings");
            st = new StringTokenizer(s);
        }
        return st.nextToken();
    }

    public boolean hasNext() throws IOException {
        while (!st.hasMoreTokens()) {
            String s = br.readLine();
            if (s == null)
                return false;
            st = new StringTokenizer(s);
        }
        return true;
    }

    public void close() throws IOException {
        br.close();
    }
}

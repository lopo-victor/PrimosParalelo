import java.io.*;
import java.util.ArrayList;

public class VerificadorPrimosParalelo {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        String arquivoEntrada = "dados/Entrada01.txt";
        String arquivoSaida = "dados/saida.txt";
        int numThreads = 10; // Número de threads para processamento

        try {
            ArrayList<Integer> numeros = lerArquivo(arquivoEntrada);
            ArrayList<Integer> primos = PrimosParalelos(numeros, numThreads);
            escreverArquivo(arquivoSaida, primos);

        } catch (IOException e) {
            e.printStackTrace();
            
        }
        long endTime = System.currentTimeMillis();
        long tempoDeExecucao = endTime - startTime;
        System.out.println("Tempo de execução: " + tempoDeExecucao);
    }

    private static ArrayList<Integer> lerArquivo(String nomeArquivo) throws IOException {
        ArrayList<Integer> numeros = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(nomeArquivo));
        String linha;
        while ((linha = reader.readLine()) != null) {
            numeros.add(Integer.parseInt(linha));
        }
        reader.close();
        return numeros;
    }

    private static void escreverArquivo(String nomeArquivo, ArrayList<Integer> primos) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(nomeArquivo));
        for (Integer primo : primos) {
            writer.write(primo + "\n");
        }
        writer.close();
    }

    private static ArrayList<Integer> PrimosParalelos(ArrayList<Integer> numeros, int numThreads) {
        ArrayList<Integer> primos = new ArrayList<>();
        Thread[] threads = new Thread[numThreads];

        for (int i = 0; i < numThreads; i++) {
             int threadIndex = i;

            threads[i] = new Thread(() -> {
                for (int j = threadIndex; j < numeros.size(); j += numThreads) {
                    int numero = numeros.get(j);
                    if (isPrimo(numero)) {
                        synchronized (primos) {
                            primos.add(numero);
                        }
                    }
                }
            });

            threads[i].start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return primos;
    }

    private static boolean isPrimo(int numero) {
        if (numero <= 1) return false;
        for (int i = 2; i <= Math.sqrt(numero); i++) {
            if (numero % i == 0) return false;
        }
        return true;
    }


}

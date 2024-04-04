import java.util.Scanner;

public class Main {
  public static void main(String[] args) {

    float notaObjetivo, promedioTotal, promedioPonderado, notaNecesaria, notaBuscada;

    System.out.print("Ingrese su nota objetivo: ");
    notaObjetivo = ingresarNotaObjetivo();            // Se ingresa la nota objetivo

    float[] p = {.15f,.25f,.25f,.20f,.15f};    // Porcentajes de las notas
    float[] n = {4.3f,1.8f,3.2f,-1f,-1f};              // Notas

    // La cantidad de porcentajes debe ser la misma que las notas. Si la nota aún no se ha subido entonces se pone -1
    // Las notas y el porcentaje se obtienen de la base de datos 

    promedioTotal = promedioTotal(p,n);      // Calcula el promedio total, contando las notas no ingresadas
    promedioPonderado = promedioPonderado(p,n);        // Calcula el promedio que lleva el estudiante
    notaNecesaria = notaNecesaria(3f,p,n);          // Calcula la nota necesaria para ganar la materia
    notaBuscada = notaNecesaria(notaObjetivo,p,n);      // Calcula la nota necesaria para llegar al objetivo
    
    System.out.println("PROMEDIO TOTAL = " + promedioTotal);
    System.out.println("PROMEDIO PONDERADO = " + promedioPonderado);
    System.out.println("NOTA NECESARIA = " + notaNecesaria);
    System.out.println("NOTA BUSCADA = " + notaBuscada);

    if (promedioPonderado < 3) {
      aviso(promedioPonderado, notaNecesaria);
    }
    
  }

  public static float promedioTotal(float[] porc, float[] notas) {

    float prom = 0;

    for (int i = 0; i < porc.length; i++) {
      if (notas[i] >= 0) {
        prom += porc[i] * notas[i];
      }
    }

    prom = (float) Math.floor(prom * 10) / 10;            // Se redondea hacia abajo
    
    return prom;
    
  }

  public static float promedioPonderado(float[] porc, float[] notas) {

    float prom = 0;
    float sum = 0;

    for (int i = 0; i < porc.length; i++) {
      if (notas[i] >= 0) {
        prom += porc[i] * notas[i];
        sum += porc[i];
      }
    }

    prom /= sum;
    prom = (float) Math.floor(prom * 10) / 10;            // Se redondea haca abajo

    return prom;

  }

  public static float ingresarNotaObjetivo() {

    Scanner sc = new Scanner(System.in);
    float nota = sc.nextFloat();
    sc.close();
    
    return nota;
    
  }

  public static float notaNecesaria(float obj, float[] porc, float[] notas) {

    float nota, sumN, sumP;

    sumN = 0;
    sumP = 1;

    for (int i = 0; i < porc.length; i++) {
      if (notas[i] >= 0) {
        sumN += porc[i] * notas[i];
        sumP -= porc[i];
      }
    }

    nota = (obj - sumN) / sumP;
    nota = (float) Math.ceil(nota * 10) / 10;              // Se redondea hacia arriba

    if (nota < 0) {            // Se iguala la nota a 0 por si el estudiante ya tiene su promedio objetivo
      nota = 0;
    }

    return nota;
    
  }

  public static void aviso(float prom, float nota) {
    System.out.println("Cuidado! Tu promedio es de " + prom);
    System.out.println("Necesitas sacar " + nota + " en el resto de notas para recuperar");
  }
  
}

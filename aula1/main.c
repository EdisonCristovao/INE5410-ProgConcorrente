#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <time.h>

int *V;
int n;
void teste(){
	V[0] = 9;
	V[1] = 8;
	V[2] = 6;
	V[3] = 4;
}
void gera_numero() {
	for (int i = 0; i < n; ++i)
		V[i] = rand()%100;
}

double media(){
	double res = 0;
	for (int i = 0; i < n; ++i)
		res += V[i];
	return res/n;
}
	
double desvio_padrao(double media){
	double res = 0;
	for (int i = 0; i < n; ++i)
		res += pow((V[i] - media),2)/n; 
	return sqrt(res);
}

void imprimir(double medi, double desv){
	printf("Os valores do vetor v são\n");
	for (int i = 0; i < n; ++i)
		printf("%d ", V[i]);
	printf("\n A media é: %f", medi);
	printf("\n o desvio é: %f", desv);
}

int main(int argc, char const *argv[]) {
	n = atoi(argv[1]);
	V = malloc(n * sizeof(int));

	gera_numero();
	//teste();
	
	double medi = media();
	double desv = desvio_padrao(medi);

	imprimir(medi, desv);
	
	return 0;
}
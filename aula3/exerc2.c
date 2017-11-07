#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <stdio.h>
#include <pthread.h>

int contador_global = 0;

void* incremente(void* numero)
{
	for (int i = 0; i < *(int*) numero; ++i)
		contador_global++;
}

int main(int argc, char const *argv[])
{
	int MAX_SIZE = atoi(argv[1]);
	pthread_t threads[MAX_SIZE];

	int n = MAX_SIZE * 1000; 

	for (int i = 0; i < MAX_SIZE; ++i)  
		pthread_create(&threads[i], NULL, incremente, (void *) &n);

	for (int i = 0; i < MAX_SIZE; ++i)
		pthread_join(threads[i], NULL);

	printf("Contador Global = %d\n", contador_global);
	return 0;
}


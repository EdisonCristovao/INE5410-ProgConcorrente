#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <stdio.h>
#include <pthread.h>

int contador_global = 0;
pthread_mutex_t mutex;

void* incremente(void* numero)
{
	for (int i = 0; i < *(int*) numero; ++i){	
		pthread_mutex_lock(&mutex);
		contador_global++;
		pthread_mutex_unlock(&mutex);
	}
}

int main(int argc, char const *argv[])
{
	int MAX_SIZE = atoi(argv[1]);
	pthread_t threads[MAX_SIZE];
	pthread_mutex_init(&mutex, NULL);

	int n = MAX_SIZE * 1000; 

	for (int i = 0; i < MAX_SIZE; ++i)  
		pthread_create(&threads[i], NULL, incremente, (void *) &n);

	for (int i = 0; i < MAX_SIZE; ++i)
		pthread_join(threads[i], NULL);

	pthread_mutex_destroy(&mutex);
	
	printf("Contador Global = %d\n", contador_global);
	return 0;
}


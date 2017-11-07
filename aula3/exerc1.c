#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <stdio.h>
#include <pthread.h>


void cria_thread(void* nome)
{
	printf("Thread %s criada com sucesso\n", (char *)nome);
}

int main(int argc, char const *argv[])
{
	pthread_t t1;
	
	pthread_create(&t1, NULL, &cria_thread, (void*) argv[1]);
	
	pthread_join(t1, NULL);
	printf("Processo principal finalizado!\n");
	return 0;
}
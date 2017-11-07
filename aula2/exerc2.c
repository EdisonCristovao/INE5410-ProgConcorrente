#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <stdio.h>

int main() {
	pid_t pid_pai = getpid();
	pid_t pid_filho2;
	pid_t pid_filho1 = fork();
	if (pid_filho1 > 0) {
		pid_filho2 = fork();
		if (pid_filho2 == 0){
			processo_filho();
			wait(NULL);
			printf("Processo %d, Finalizado \n", getpid());
		}
	} else if(pid_filho1 == 0){
		processo_filho();
		wait(NULL);
		printf("Processo %d, Finalizado \n", getpid());
	}

	if (getpid() == pid_pai) {
		wait(NULL);
		printf("Processo principal finalizado");
	}
	
	return 0;
}

void processo_filho(){
	printf("Processo %d, filho de %d \n", getpid(),getppid());
	pid_t pid_filho1 = fork();
	wait(NULL);
	if (pid_filho1 > 0) {
		pid_t pid_filho2 = fork();
		wait(NULL);
		if (pid_filho2 > 0) {
			pid_t pid_filho3 = fork();
			wait(NULL);
			if (pid_filho3 == 0){
				printf("Processo %d, filho de %d \n", getpid(),getppid());
				sleep(5);
				printf("Processo %d, Finalizado \n", getpid());
				exit(NULL);
			}
		}
		if (pid_filho2 == 0) {
			printf("Processo %d, filho de %d \n", getpid(),getppid());
			sleep(5);
			printf("Processo %d, Finalizado \n", getpid());
			exit(NULL);
		}
	} else if (pid_filho1 == 0) {
		printf("Processo %d, filho de %d \n", getpid(),getppid());
		sleep(5);
		printf("Processo %d, Finalizado \n", getpid());
		exit(NULL);
	}
}

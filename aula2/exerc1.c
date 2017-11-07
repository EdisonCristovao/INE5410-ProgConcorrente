#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <stdio.h>

int main() {
	pid_t pid_pai = getpid();
	pid_t pid_filho2;
	pid_t pid_filho = fork();
	if (pid_filho > 0) {
		pid_filho2 = fork();
		if (pid_filho2 == 0){
			printf("Processo filho %d criado \n", getpid());
		}
	} else if(pid_filho == 0){
		printf("Processo filho %d criado \n", getpid());
	}

	if (getpid() == pid_pai) {
		printf("Processo pai criou %d e o %d\n", pid_filho , pid_filho2);
	}
	
	return 0;
}
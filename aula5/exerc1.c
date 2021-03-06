#include <stdlib.h>
#include <stdbool.h>
#include <unistd.h>
#include <sys/types.h>
#include <stdio.h>
#include <pthread.h>
#include <time.h>
#include <semaphore.h>

int produzir(int *seed);
void consumir(int produto);
void *ProdutorFunc(void *arg);
void *ConsumidorFunc(void *arg);

int indice_produtor, indice_consumidor, tamanho_buffer;
int* buffer;

//Você deve fazer as alterações necessárias nesta função e na função
//ConsumidorFunc para que usem semáforos para coordenar a produção
//e consumo de elementos do buffer.
void *ProdutorFunc(void *arg) {
    int seed = time(NULL); //Seed para o gerador de números aleatórios
    
    while (true) {
        int produto = produzir(&seed); //produz um elemento usando o seed do parâmetro
        indice_produtor = (indice_produtor + 1) % tamanho_buffer; //calcula o próximo elemento
        buffer[indice_produtor] = produto; //adiciona o elemento produzido à lista
    }
}

void *ConsumidorFunc(void *arg) {
    while (true) {
        int produto;
        indice_consumidor = (indice_consumidor + 1) % tamanho_buffer; //Calcula o próximo item a consumir
        produto = buffer[indice_consumidor]; //obtém o item da lista
        consumir(produto); //Consome o item obtido.
    }
}


int main(int argc, char *argv[]) {
    if (argc < 2) {
        printf("Use: %s [tamanho do buffer]\n", argv[0]);
        return 0;
    }

    tamanho_buffer = atoi(argv[1]);
    indice_produtor = 0;    
    indice_consumidor = 0;

    //Iniciando buffer
    buffer = malloc(sizeof(int) * tamanho_buffer);

    //Aqui você deve criar as threads e semáforos necessários
    //para que o código funcione adequadamente.
    
    //Libera memória do buffer
    free(buffer);

    return 0;
}

//Esta função produz um elemento. O argumento seed é usado para gerar
//um número aleatório, e é necessário por que a função rand() não
//é thread-safe e tem comportamento indefinido se usada por várias
//threads ao mesmo tempo.
int produzir(int *seed) {
    int produto = 500 + (rand_r(seed) % 500);
    printf("Produzindo %d\n", produto);
    usleep(produto * 1000);
    return produto;
}

//Esta função consome um elemento.
void consumir(int produto) {
    printf("Consumindo %d\n", produto);
    usleep(produto * 1000);
}
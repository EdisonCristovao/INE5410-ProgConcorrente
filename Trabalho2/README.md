trabalho 2
Neste trabalho você deve implementar um programa concorrente em Java, que será executado simultaneamente em diversas máquinas, com o intuito de descobrir qual entrada, ao ser passada para uma função de hash (resumo), gera um determinado código de hash. 

No arquivo hashes.txt estão listados códigos produzidos pela função de hash MD5 a partir de alguns números de 7 dígitos. Você deve escrever um programa para descobrir qual número produziu cada código contido neste arquivo. Você deve testar, um a um, todos os números possíveis (de 0000000 a 9999999), usando o método md5(), até encontrar o número que produz o código em questão. O arquivo CodigoSequencial.java tem a implementação sequencial desta operação.

Você deve usar esse programa como base para escrever o seu programa concorrente. Você deve ter um servidor e vários clientes. O servidor deve coordenar o trabalho, distribuindo um código e uma faixa de números para cada cliente. Os clientes devem testar os números desta faixa até que seja encontrado o código em questão. Por exemplo: o primeiro cliente a se conectar no servidor recebe o primeiro código e a faixa de 0000000 a 0199999 (ou seja, os primeiros 200 mil números) e deve testar todos os números nessa faixa, passando cada número para o método md5() e comparando o código gerado com o código desejado. Caso eles sejam iguais, o número desejado foi encontrado. O segundo cliente a se conectar também deve receber o mesmo código e uma outra faixa de números para testar (nesse caso, 0200000 a 0399999) e assim por diante. Se o cliente passar por todos os números de sua faixa sem encontrar o código correto, deve pedir para o servidor a próxima faixa para testar. Assim que um cliente encontrar o número desejado, ele deve informar ao servidor. O servidor, por sua vez, deve imprimir no console o número recebido e passar para o próximo código do arquivo, que deve ser enviado para todos os clientes conectados juntamente com uma nova faixas de números a serem testados por cada cliente. Ao receber o novo código e faixa, os clientes que ainda estiverem executando a requisição anterior devem abandoná-la, e em seguida começar a executar a nova requisição do servidor.

Instruções
Você deve usar sockets para a comunicação entre o servidor e os clientes.
O servidor deve imprimir no console sempre que enviar códigos e faixas de números para os clientes testarem.
O servidor deve armazenar em um arquivo de saída os números encontrados pelos clientes.
Use uma faixa de 200.000 números para cada cliente.
Sempre formate o número com 7 dígitos para usar o método md5() (veja o exemplo no arquivo CodigoSequencial.java).
Os clientes devem imprimir no console quando recebem faixas para processar e quando finalizam uma faixa (tendo encontrado ou não o número correto).


___
# Avaliação DBC Company
Este projeto tem como finalidade implementar as soluções ao [exercício proposto](https://drive.google.com/file/d/1Ub6yCGiBHyZnnngWFZ-DjSQ9CruvVKqP/view?usp=sharing) pela equipe da [DBC Company](https://www.dbccompany.com.br/) para o seu processo seletivo.


### Organização do projeto

Ao analisar o execício proposto para esse processo seletivo meu primeiro impulso foi desenvolver o projeto utilizando spring boot com os frameworks para web e uma interface em vuejs para visualização do relatório. Contudo, analisando melhor os requisitos levantados decidi por considerar alguns princípios da programação, começando pelo "YAGNI", que tem como premissa básica fazer o que é realmente necessário não adicionando features extras sem a real necessidade. Este princípio é importante pois faz com que focamos no que realmente foi pedido, não correndo riscos desnecessários para a performance e corretude do código implementado. Os outros dois princípios são basicamente boas práticas de programação os quais seriam o "KISS", que tem como premissa manter o código o mais simples possível, evitando complexidades desnecessários. E por fim, temos o "DRY", que é evitar a repetição de código. Para isso basta utilizar as melhores práticas da orientação a objetos e separação de responsabilidades.
Feita essa pequena introdução, exponho que para a implementação desse projeto foi utilizado o *"starter"* do spring boot para execução de batchs através do framework **Spring Batch**. Visto que o código deve continuar rodando e escutando alterações realizadas em um determinada pasta decidi utilizar uma implementação do próprio java para realizar essa tarefa o [WatchService](https://docs.oracle.com/javase/8/docs/api/java/nio/file/WatchService.html). Sendo assim estruturei o projeto da seguinte forma:
![enter image description here](https://i.ibb.co/XZt9Ftd/image.png)

Separei as responsabilidades de cada trecho do código em pacotes. Visto que o Spring batch utilizar os conceitos de "jobs", "steps", "readers" e "writers", ficando o job e o step(devido a simplicidade foi necessário apenas um step) no pacote de configuração do processo e o reader e o writer em seus respectivos pacotes. No pacote models ficam os POJOs e as classes de negócio referentes ao modelo, como a classe "DbcDataResult". Neste pacote podemos observar a utilização de alguns princípios como o "DRY". Para a criação dos objetos do pacote de models foi criado um factory, que tem como objetivo simplificar e centralizar a criação dessas instâncias. Por fim temos o pacote de services que contem o serviço responsável por escutar alterações na pasta selecionada e disparar a execução do job responsável por ler os arquivos de análise e geração do arquivo de resultados.
O projeto em si foi dividido em dois pacotes com o objetivo de separar os dois exercícios propostos, e dentro desses pacotes foram criados outros pacotes de forma a haver uma separação mais clara entre as camadas de cada exercício.

_____

###### Anotações:
______

Inicialmente comecei a implementação do projeto utilizando práticas de TDD na classe de factory dos modelos, contudo comecei a ter alguns problemas  alguns problemas quando tentei utilizar essa prática na implementação da estrutura do Spring batch, visto que muita das suas implementações são internas fazendo assim com que fosse necessário implementar várias classes apenas para conseguir realizar os testes antes, quebrando assim os princípios do "YAGNI" e do "KISS". Por este motivo, decidi criar alguns testes depois para validar as funcionalidades.

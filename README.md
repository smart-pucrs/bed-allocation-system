# Bed Allocation System

By [Débora C. Engelmann](https://github.com/DeboraEngelmann),
[Lucca Dornelles Cezar](https://github.com/rukndf),
[Alison R. Panisson](https://github.com/AlisonPanisson) and
[Rafael H. Bordini](https://github.com/rbordini)

You will find all the necessary documentation in our [Wiki](https://github.com/smart-pucrs/explainable_agents/wiki) (Quando ela existir...).

> Part of the code used in this project is authored by [Débora Engelmann](https://github.com/DeboraEngelmann), 
[Juliana Damasio Oliveira](https://github.com/julianadamasio), 
[Olimar Teixeira Borges](https://github.com/olimarborges), 
[Tabajara Krausburg](https://github.com/TabajaraKrausburg), 
[Marivaldo Vivan](https://github.com/Vivannaboa)  and
[Rafael H. Bordini](https://github.com/rbordini) Available in [Dial4JaCa](https://github.com/smart-pucrs/Dial4JaCa).

## Arquivos 
- aloc_agent.asl

  Agente de alocação(temp).
- DBConnect.java 

  Objeto que interage com o GLPSOL(requer que a string 'GLPSol' aponte para o arquivo correto)
- FStore.java

  Objeto que implementa o DBConnect, configurado para ler o input do firebase. 
- FStoreArtifact.java 

  Artefato que gerencia as operações do agente com o objeto FStore 

## To run this project you need

- Java 11
- Gradle 6.8.3
- JaCaMo  0.9
- [GLPSol 4.6+](https://github.com/smart-pucrs/bed-allocation-system/wiki/Installing-GLPSol)

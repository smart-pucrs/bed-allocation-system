# Explainable Agents

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
[Rafael H. Bordini](https://github.com/rbordini) Available in [helloworld_from_jason](https://github.com/DeboraEngelmann/helloworld_from_jason).

## Arquivos 
- aloc_agent.asl

  Agente de alocação(temp).
- DBConnect.java 

  Objeto que interage com o GLPSOL(requer que a string 'GLPSol' aponte para o arquivo correto)
- FStore.java

  Objeto que implementa o DBConnect, configurado para ler o input do firebase. 
- FStoreArtifact.java 

  Artefato que gerencia as operações do agente com o objeto FStore 

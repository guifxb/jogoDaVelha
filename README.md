

<img src="https://github.com/guifxb/jogoDaVelha/assets/107308140/79c84138-f251-4b58-bc52-000be52cb4ae" alt="Start Screen" width="750" height="500" style="border-radius: 12px;">

# Jogo da Velha

Bem-vindo ao Jogo da Velha, um aplicativo divertido e desafiador desenvolvido em Kotlin, seguindo a arquitetura MVVM e utilizando as bibliotecas Koin e Room para fornecer uma experiência completa. Este aplicativo permite partidas entre dois jogadores no mesmo dispositivo ou contra uma inteligência artificial (IA), com suporte para tabuleiros de 3x3 até 10x10.

## Funcionalidades

### Modos de Jogo
1. **Dois Jogadores:** Desafie um amigo para uma partida no mesmo dispositivo.
2. **Jogador vs. IA:** Teste suas habilidades contra a inteligência artificial do jogo.

### Tamanhos de Tabuleiro
- Escolha entre tabuleiros de 3x3 até 10x10 para uma variedade de desafios.

### Persistência de Histórico
- O app utiliza a biblioteca Room para persistir o histórico de partidas, mantendo um registro das partidas anteriores.

### Anúncios com Google AdMob
- Suporte a anúncios do Google AdMob.

## Dependências

- [Kotlin](https://kotlinlang.org/)
- [Koin](https://insert-koin.io/)
- [MVVM](https://developer.android.com/jetpack/guide)
- [Room](https://developer.android.com/jetpack/androidx/releases/room)
- [Google AdMob](https://developers.google.com/admob)

## Estrutura do Projeto

O projeto segue a arquitetura MVVM para uma organização clara e modular do código. Aqui estão algumas das pastas principais:

- **`data`:** Contém as classes relacionadas à persistência de dados.
- **`domain`:** Define os casos de uso e as entidades do domínio.
- **`presentation`:** Contém as classes responsáveis pela interação com o usuário.

### Estrutura Detalhada

- **`data`**
  - `dao`: Classes de acesso a dados para interação com o Room.
  - `database`: Gerenciamento do banco de dados
  - `repository`: Implementações de repositórios para acessar e manipular dados.
  
- **`domain`**
  - `model`: Definições das entidades do domínio.

- **`presentation`**
  - `navigation`: NavController para gerenciar as telas
  - `di`: Configuração do Koin para injeção de dependência.
  - `view`: Classes de atividades, fragmentos e interfaces de usuário.
  - `viewmodel`: ViewModels responsáveis pela lógica de apresentação.


<p align="center">
  <img src="https://github.com/guifxb/jogoDaVelha/assets/107308140/70725af0-f896-41e9-bcae-205e42eb5c92" alt="Play Store Icon" width="200" height="200">
</p>


## Disponível na Play Store

Você pode baixar e experimentar o Jogo da Velha Multiplayer na [Play Store](https://play.google.com/store/apps/details?id=com.gfbdev.jogoDaVelha). Aproveite e compartilhe com seus amigos para desafiá-los em partidas emocionantes!

<p align="center">
<a href='https://play.google.com/store/apps/details?id=com.gfbdev.jogoDaVelha&pcampaignid=web_share&pcampaignid=pcampaignidMKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1'><img alt='Get it on Google Play' src='https://play.google.com/intl/en_us/badges/static/images/badges/en_badge_web_generic.png' width="260" /></a>
</p>

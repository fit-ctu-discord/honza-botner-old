# Botner
## Discord bot pro fakultní server FIT ČVUT

## Instalace, spuštění lokální instance

V první řádě je potřeba na čílovém PC rozchodit [Docker](https://docker.com)

Pro správné spuštění je potřeba v resources (./src/main/resources/) nakopírovat soubor **.env.example** do
souboru **.env** (tedy jen odebrat .example na konci) a tam nastavit patřičné hodnoty.

Pro sestavení Docker containeru je pak potřeba zavolat:

`docker build -t botner . `

Poté, co se container sestaví, je možné ho spusit pomocí příkazu:

`docker run --rm -it botner:latest`

Zatím that's all folks.
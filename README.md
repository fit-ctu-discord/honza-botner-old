# Botner

![Logo](./docs/images/logo.png)

## Discord bot pro fakultní server FIT ČVUT

## Instalace, spuštění lokální instance

V první řádě je potřeba na cílovém PC rozchodit [Docker](https://docker.com).

Pro správné spuštění je potřeba nakopírovat soubor **.env.example** do
souboru **.env** (tedy jen odebrat .example na konci) a tam nastavit patřičné hodnoty.

Dále je možné upravit **config.development.json**, kde je konfigurace spojená se serverem.
Cestu k tomuto souboru je potřeba nastavit v **.env** jako hodnotu klíče `CONFIG_FILE`.

Pro sestavení Docker containeru je pak potřeba zavolat:
`docker build -t botner . `

Poté, co se container sestaví, je možné ho spusit pomocí příkazu:
`docker run -it -p 80:80 botner:latest`

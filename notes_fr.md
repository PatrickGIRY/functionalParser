# Analyseur syntaxique fonctionnel

## Qu'est qu'un analyseur syntaxique ?

> Un analyseur syntaxique analyse une chaîne de caractères pour produire un arbre syntaxique.

## Définissons un analyseur syntaxique par une fonction

> En java nous pouvons définir une fonction à l'aide d'une interface

```java
@FunctionalInterface
public interface Parser {
    Tree parse(String input);
}
```

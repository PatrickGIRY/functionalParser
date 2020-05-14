# Analyseur syntaxique fonctionnel

## Qu'est qu'un analyseur syntaxique ?

Un analyseur syntaxique analyse une chaîne de caractères pour produire un arbre syntaxique.

## Définissons un analyseur syntaxique par une fonction

Nous pouvons définir un analyseur syntaxique comme une fonction qui prend en entrée une chaîne de caractères et qui produit en résultat un arbre.

> En java nous pouvons définir une fonction à l'aide d'une interface

```java
@FunctionalInterface
public interface Parser {
    Tree parse(String input);
}
```

Cette définition est correcte, mais pas pratique. 
Elle n'est pas composable.
Nous voudrions décomposer cet analyseur syntaxique en analyseurs syntaxiques plus simples.

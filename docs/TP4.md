# Minipaint 
## TP4 – On continue...

### 📟 Retour visuel
---

Dans la partie précédente, on a implémenté une fonctionnalité permettant de sélectionner plusieurs formes à la fois.

>**🖥 TODO**
>
> - Modifiez la classe `StatutBar` pour qu’elle affiche aussi le nombre de formes sélectionnées. 

<br>
 
🔕 Problème : le nombre de formes sélectionnées ne se met pas à jour automatiquement. C’est normal, puisque les observateurs du `DrawingPane` sont avertis lorsqu’une forme est ajoutée ou supprimée, mais pas quand une forme est sélectionnée. 

>**🖥 TODO**
>
> - Proposez une solution pour que les observateurs du `DrawingPane` soient aussi avertis lorsque la sélection change. Attention, veillez à respecter le principe de la responsabilité des classes... 
> - Implémentez la solution. 
> - Écrivez les tests nécessaires.

<br> 

### 🗑 Suppression de forme
---
Grâce à la sélection multiple, on peut donc appliquer des actions sur toutes les formes sélectionnées. On a déjà implémenté le déplacement multiple. Continuons avec la suppression de forme(s). 

>**🖥 TODO**
>
> - Ajoutez les classes nécessaires et un bouton dans l’interface. 
> - Écrivez les tests correspondants.

🪲 Oui mais… il reste un « bug » : lorsqu’on supprime une ou plusieurs forme(s), la liste des formes sélectionnées n’est pas vidée ! 

>**🖥 TODO**
>
> - Corrigez cela… 🩹

<br> 

### 🗄 Un peu de rangement
---

>**🖥 TODO**
>
> - Créez une classe `ToolBar` qui va contenir tous les boutons de l’interface, et qui se chargera de les instancier. Modifiez la classe `PaintApplication` en conséquence. 
> - Toutes les classes sont pour l’instant dans le même et unique package `drawing`. Créez les packages `drawing.shapes`, `drawing.ui`, `drawing.handlers`. Déplacez les classes dans les bons packages.


Voilà qui a un peu plus d’allure ! 👔

<br>

### 🏭 Une fabrique « simple »
---
On voudrait remplacer le texte sur les boutons par des icônes. Chaque action aura son icône dédiée, et le texte deviendra une info-bulle sur le bouton. Cherchez comment ajouter une image sur un objet de type `Button` dans l’API JavaFx. 

Vous pourrez récupérer des icônes libres de droit sur [https://material.io/tools/icons](https://material.io/tools/icons)

>**🖥 TODO**
>
> - Dans la classe `ToolBar`, rajoutez les lignes de code nécessaires à l’ajout d’image pour chaque bouton. 

<br>

Vous remarquez que la création de chaque bouton fait intervenir les mêmes lignes de code redondantes. C’est là qu’intervient la *Fabrique « simple »*. Ce n’est pas vraiment un patron de conception, mais plutôt une bonne pratique. 

L’idée consiste à regrouper l’instanciation des boutons dans une seule classe `ButtonFactory`, qui sera donc notre « usine » à boutons. Cette classe contiendra une méthode :

	public Button createButton(String buttonName)
	
>**🖥 TODO**
>
> - Créez cette classe et implémentez la méthode `createButton`. La classe `ButtonFactory` pourra proposer des constantes (sous forme d’attributs statiques publics) définissant la liste des valeurs que peut prendre le paramètre `buttonName`. Ce paramètre permettra à la méthode `createButton` de déterminer quelle image doit être chargée, et quel texte doit être mis dans l’info-bulle.
> - Dans la `ToolBar`, faites maintenant appel à cette fabrique pour obtenir les instances de boutons dont vous avez besoin.

<br> 

Maintenant, on voudrait qu’il soit toujours possible de créer des boutons sans icone, avec seulement du texte. 

>**🖥 TODO**
>
> - Pour cela, rajoutez un paramètre `style` au constructeur de la `ButtonFactory`, qui pourra prendre 2 valeurs possibles (définies dans des constantes `ICONS_ONLY` et `TEXT_ONLY`). 
> - Modifiez la méthode `createButton` pour qu’elle instancie les boutons et les configure de la manière voulue, en fonction du style défini lors de l’instanciation de la fabrique.
> - Testez !

<br>

### 🏗 Mais alors, le patron « Factory Method », qu’est-ce que c’est-y donc ?
---
Pour plus de détails sur ce patron, suivez la [vidéo de Christopher Okhravi](https://youtu.be/EcFVTgRHJLM) 📺

En fait, ce patron est déjà présent dans l’application, et vous l’avez utilisé sans vous en rendre compte… 🥷

>**🖥 TODO**
>
> - Observez la classe `ShapeButtonHandler` et les classes qui en héritent. Un petit diagramme de classes pourra vous aider…  

Alors, vous avez trouvé ?

<br>

[🔙 Retour](../README.md)

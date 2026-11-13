# Reviews Z-Link — cadrage du parcours frontend

## Objectif du document

Ce document décrit le parcours de suivi d’une demande et de publication des avis, afin que le frontend puisse préparer ses écrans et donner son avis sur l’expérience proposée. Il s’agit d’une proposition métier à discuter, pas encore d’un contrat API final.

## Contexte produit

Z-Link aide les clients à découvrir des prestataires locaux, consulter leurs profils et leurs avis, puis envoyer une demande de service. Le prestataire peut accepter ou refuser. Après l’acceptation et l’échange des coordonnées, la négociation, le paiement et la réalisation se déroulent en dehors de la plateforme.

Z-Link ne peut donc pas vérifier directement qu’un contact, un accord ou une prestation a eu lieu. Le suivi recueille les déclarations indépendantes des deux parties et doit montrer clairement ce qui est confirmé, contesté ou sans réponse. Un avis est un signal de réputation, pas une certification de la prestation.

## Principes du parcours

1. Une demande `ACCEPTED` ne suffit pas à autoriser un avis.
2. Le client peut déposer un avis après avoir déclaré que la prestation a été réalisée.
3. Le silence du prestataire ne bloque pas la publication et n’est jamais interprété comme une réponse négative ou positive.
4. Une contestation du prestataire n’efface pas automatiquement l’avis ; elle est conservée comme information de contexte et peut être examinée par la modération.
5. Une seule review est permise par demande et par client.
6. Le suivi est toujours consultable dans le détail de la demande. Les notifications y conduisent, mais ne remplacent pas cet espace.
7. Le profil public distingue le contenu de l’avis de son niveau de confirmation.

## Flow métier complet

### 1. Demande en attente

Le client a envoyé une demande au prestataire.

- Statut de demande : `PENDING`.
- Le suivi et le dépôt d’avis ne sont pas encore ouverts.
- Si le prestataire refuse ou si le client annule à ce stade, aucun avis n’est possible.

### 2. Demande acceptée

Le prestataire accepte et le client peut accéder à ses coordonnées.

- Statut de demande : `ACCEPTED`.
- Une section permanente **Suivi de la prestation** apparaît dans le détail de la demande pour les deux parties.
- Le suivi commence par la question : **« Êtes-vous parvenus à un accord pour réaliser ce service ? »**
- Chaque partie répond en son propre nom : **Oui** ou **Non**.
- Il est possible de fournir une date prévisionnelle de réalisation. Elle reste facultative.
- La prise de contact peut être recueillie comme information de diagnostic, mais ne devrait pas être une condition bloquante : être en contact ne prouve pas qu’un accord ou une prestation existe.

États possibles de l’accord :

- `WAITING_FOR_RESPONSES` : une ou deux réponses sont attendues.
- `AGREED` : les deux parties ont répondu Oui.
- `NO_AGREEMENT` : les deux ont répondu Non.
- `DISPUTED` : les réponses sont différentes.
- `NO_RESPONSE` : délai expiré sans réponse complète. Ce résultat n’équivaut pas à Non.

Si l’accord est `NO_AGREEMENT`, aucun avis n’est disponible pour le moment. Si les réponses divergent ou manquent, le suivi reste consultable et indique les réponses en attente ou le désaccord, sans prétendre que la prestation est confirmée.

### 3. Attente de la réalisation

Quand les deux parties confirment l’accord, le suivi peut afficher la date prévue si elle a été renseignée.

- À la date prévue, ou après un délai raisonnable si aucune date n’a été donnée, le client est invité à indiquer si le service a été réalisé.
- Le prestataire est invité séparément à donner sa réponse.
- Les réponses sont **Oui** ou **Non** ; chacune est associée à son auteur et à sa date.
- Une réponse peut être corrigée tant que le suivi est ouvert, avec conservation de l’historique.

Le frontend ne doit pas transformer une absence de réponse en réponse négative. Il présente par exemple **« En attente de réponse »** ou **« Aucune réponse reçue »**.

### 4. Déclaration de réalisation et éligibilité à l’avis

La règle proposée est que **la réponse Oui du client ouvre immédiatement le dépôt d’avis**, même si le prestataire n’a pas répondu ou conteste. L’avis ne doit pas attendre une confirmation mutuelle.

Les résultats visibles du suivi sont notamment :

| Réponse client | Réponse prestataire | Éligibilité du client | Contexte de l’avis |
|---|---|---|---|
| Oui | Oui | Avis autorisé | Réalisation déclarée par les deux parties |
| Oui | Aucune réponse | Avis autorisé | Déclarée par le client, sans réponse du prestataire |
| Oui | Non | Avis autorisé | Réalisation contestée par le prestataire |
| Non | Non | Pas d’avis pour l’instant | Les deux déclarent que le service n’a pas été réalisé |
| Non | Oui ou aucune réponse | Pas d’avis pour l’instant | Réponses divergentes ou incomplètes ; suivi à clarifier |

Si le client répond Non mais que la situation évolue plus tard, il doit pouvoir actualiser sa réponse ou demander la réouverture du suivi selon la politique produit.

### 5. Rédaction et publication de la review

Le formulaire client contient :

- une note obligatoire de 1 à 5 étoiles ;
- un commentaire facultatif ;
- une action explicite de publication ;
- un rappel que l’avis sera visible sur le profil public du prestataire.

Après publication :

- l’avis apparaît sur le profil du prestataire et dans la demande ;
- la moyenne et la distribution des notes sont calculées uniquement avec les reviews publiées et non masquées par la modération ;
- le niveau de confirmation de la réalisation reste associé à l’avis ;
- le prestataire peut répondre à l’avis et/ou le signaler ;
- le client ne peut pas créer un second avis pour cette demande.

Un avis contesté reste publié et comptabilisé, sauf décision de modération. Le frontend ne doit pas masquer automatiquement l’avis ni retirer la note de la moyenne à cause d’une contestation seule.

## Section « Suivi de la prestation »

Elle est présente dans le détail d’une demande acceptée pour le client et le prestataire. Chaque utilisateur ne répond qu’en son propre nom.

La section devrait afficher :

- l’étape actuelle et une progression simple (accord → réalisation → avis) ;
- les actions actuellement disponibles pour l’utilisateur connecté ;
- sa propre réponse et sa date ;
- l’état de la réponse de l’autre partie : répondue, en attente ou délai expiré ;
- la date prévisionnelle, si connue ;
- la prochaine étape et son échéance, lorsqu’elles sont définies ;
- le résultat final et le lien vers la review si elle existe.

Pour limiter les réponses influencées, envisager de ne pas révéler la réponse de l’autre partie avant que l’utilisateur ait répondu lui-même ou avant la clôture de l’étape. À discuter avec l’équipe produit.

Une seule action principale est présentée à la fois. Les étapes futures peuvent être visibles mais désactivées avec une explication courte.

## Notifications

Les notifications déclenchent une action, mais toute l’information reste disponible dans le suivi de la demande.

Événements pouvant produire une notification :

- nouvelle demande à traiter par le prestataire ;
- demande acceptée ;
- réponse de l’autre partie sur l’accord ;
- rappel si une réponse à l’accord est attendue ;
- invitation à renseigner ou confirmer la date prévue ;
- invitation à répondre sur la réalisation quand le moment est venu ;
- réponse de l’autre partie sur la réalisation ;
- invitation du client à publier une review après sa déclaration de réalisation ;
- publication d’une review ou réponse du prestataire, si ces événements sont notifiés.

Chaque notification renvoie au détail de la demande, idéalement à la section de suivi ou au formulaire correspondant. Prévoir un état de chargement, une confirmation après action et une gestion compréhensible des erreurs (étape expirée, déjà répondue, droits insuffisants).

## Délais proposés pour discussion

Les délais sont des valeurs de départ à valider avec le produit ; ils ne doivent pas devenir une preuve de réalisation.

| Moment | Proposition initiale |
|---|---|
| Première demande de confirmation d’accord | Après acceptation, éventuellement avec un court délai avant la sollicitation |
| Rappel sur l’accord | 48 h après la sollicitation |
| Clôture du suivi faute de réponses | Après environ 7 jours ; état `NO_RESPONSE`, jamais réponse Non |
| Question sur la réalisation | À la date prévue ou quelques jours après l’accord si aucune date n’existe |
| Rappel sur la réalisation | Après quelques jours, puis clôture du suivi après une échéance configurée |
| Invitation à l’avis | Dès que le client répond Oui à la réalisation |
| Réponse du prestataire à la réalisation | Peut être sollicitée pendant une fenêtre courte, par exemple 72 h ; son expiration ne bloque pas l’avis |

Le client doit pouvoir signaler un report ou une prestation nécessitant plusieurs jours/interventions. Pour le MVP, on peut traiter la demande comme une prestation globale et n’autoriser l’avis qu’après la fin de l’ensemble convenu.

## Écrans et états frontend à prévoir

1. **Détail d’une demande PENDING** : attente de décision du prestataire, pas de suivi de réalisation ni d’avis.
2. **Demande ACCEPTED / accord à renseigner** : formulaire d’accord pour l’utilisateur ; état de réponse de l’autre partie.
3. **Accord confirmé / attente de réalisation** : date prévue facultative et indication de la prochaine sollicitation.
4. **Question de réalisation** : Oui / Non pour chaque partie, avec possibilité de reporter si prévue par le produit.
5. **Client éligible à la review** : formulaire note/commentaire et explication de la visibilité publique.
6. **Review publiée** : confirmation, lien vers l’avis et contexte de confirmation.
7. **Désaccord / contestation** : état neutre, réponses enregistrées, consigne de contacter le support si nécessaire ; ne pas afficher une accusation comme un fait établi.
8. **Suivi expiré ou sans réponse** : expliquer ce qui est clôturé, ce qui reste possible et pourquoi l’absence de réponse n’est pas une réponse négative.

## Contrat de données utile au frontend

Le backend devrait fournir, pour le suivi, au minimum :

- `serviceRequestId`, `currentUserRole` (client/prestataire) ;
- `agreementStatus` et la réponse courante de chaque partie avec date ;
- `completionStatus` et la réponse courante de chaque partie avec date ;
- `scheduledAt` facultatif ;
- `currentStep`, `nextAction`, échéance et état d’expiration ;
- `canSubmitReview`, `reviewId` éventuel et état de la review ;
- `reviewEvidenceLevel` ou équivalent pour le libellé public.

Les noms ci-dessus sont indicatifs, pas une API arrêtée. Il est préférable que le backend fournisse les permissions/actionnabilités (`canRespond`, `canSubmitReview`) plutôt que de faire reconstituer toutes les règles métier par le frontend. L’identité du répondant doit venir de la session authentifiée, jamais d’un identifiant fourni librement par le client web.

Libellés publics suggérés :

- **Confirmée par le client et le prestataire**
- **Déclarée par le client — sans réponse du prestataire**
- **Réalisation contestée par le prestataire**

Ces libellés décrivent uniquement les réponses reçues par Z-Link ; ils ne doivent pas être formulés comme une vérification indépendante.

## Questions pour recueillir l’avis du frontend

Merci d’évaluer notamment :

1. Le parcours accord → réalisation → avis est-il compréhensible sans trop d’écrans ?
2. La section de suivi est-elle facile à retrouver depuis la demande et les notifications ?
3. Comment représenter simplement l’attente, le désaccord, l’absence de réponse et la contestation ?
4. Faut-il afficher les réponses de l’autre partie immédiatement, ou seulement après sa propre réponse ?
5. La saisie d’une date prévue est-elle compréhensible et utile sur mobile ?
6. Les libellés de confiance proposés sont-ils clairs, neutres et non trompeurs ?
7. Quels états de chargement, erreurs, rappels ou cas limites manquent à cette proposition ?

## Décisions produit restant à valider

- Les délais exacts de sollicitation, rappel et clôture.
- La politique si le client répond Non à la réalisation, puis change d’avis.
- Si une contestation entraîne seulement un badge et un droit de réponse, ou aussi une file de modération.
- Si le client peut modifier son avis après publication, et dans quel délai.
- Le traitement des prestations multi-interventions et des demandes récurrentes.
- La possibilité d’une review réciproque du client par le prestataire (hors périmètre de cette proposition).
- Les catégories de modération et leurs effets sur l’affichage et le calcul de la note.

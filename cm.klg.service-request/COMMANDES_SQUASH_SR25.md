# Commandes utilisées pour le squash SR-25

Ce document explique les commandes exécutées pour regrouper SR-25 à SR-36 dans un seul commit. Les chemins temporaires sont abrégés en `<tmp>`. Aucune commande de push n'a été exécutée.

> **Attention :** ce journal décrit une opération déjà réalisée; il ne faut pas rejouer toutes les commandes telles quelles. Plusieurs SHA et références de stash sont propres à l'état de cette opération.

## Vérification de l'historique et de l'état Git

```bash
git merge-base working/SR-25 develop
```
Affiche le commit commun le plus récent entre SR-25 et `develop`, c'est-à-dire la base à partir de laquelle la branche a divergé.

```bash
git rev-list --count develop..working/SR-36
```
Compte les commits présents sur SR-36 mais absents de `develop`; cela permet de vérifier l'étendue de l'historique à regrouper.

```bash
git log --oneline --reverse develop..working/SR-36
```
Affiche ces commits dans l'ordre chronologique, du plus ancien au plus récent.

```bash
git status --short
```
Montre les fichiers modifiés, indexés ou non suivis dans le worktree courant.

```bash
git worktree list --porcelain
```
Liste les worktrees Git et les branches qui les occupent, pour éviter de modifier une branche utilisée ailleurs.

```bash
git ls-remote --heads origin
```
Interroge les références de branches du remote `origin` sans les télécharger ni les modifier. La vérification a confirmé qu'aucune branche SR concernée n'y était présente.

## Sauvegarde des changements locaux et préparation du squash

```bash
git stash push --include-untracked -m 'SR-36 worktree snapshot for SR-25 squash'
```
Sauvegarde temporairement les changements suivis et non suivis (y compris les fichiers non suivis) afin de préserver le worktree avant la préparation du commit.

```bash
git worktree add --detach '<tmp>/sr25-squash' develop
```
Crée un worktree temporaire séparé, positionné sur `develop` en mode détaché. La branche principale et son worktree restent ainsi intacts pendant la préparation.

```bash
git -C '<tmp>/sr25-squash' merge --squash working/SR-36
```
Applique dans le worktree temporaire le résultat cumulé de SR-36, sans créer de commit de merge. Les changements sont préparés comme une seule modification par rapport à `develop`.

```bash
git -C '<tmp>/sr25-squash' stash apply 'stash@{0}'
```
Réapplique dans le worktree temporaire les changements locaux sauvegardés. `apply` conserve la sauvegarde dans la liste des stashes tant qu'elle n'est pas explicitement supprimée.

```bash
git -C '<tmp>/sr25-squash' add -A
```
Indexe tous les changements du worktree temporaire, y compris les ajouts, modifications et suppressions, pour qu'ils soient inclus dans le commit.

```bash
git -C '<tmp>/sr25-squash' diff --cached --check
```
Vérifie les changements indexés et signale notamment les erreurs d'espacement, comme les espaces en fin de ligne.

## Initialisation et validation

```bash
git -C '<tmp>/sr25-squash' submodule update --init --recursive
```
Initialise les sous-modules requis par le dépôt et les place sur les révisions enregistrées dans le projet.

```bash
cd '<tmp>/sr25-squash/cm.klg.service-request' && ./gradlew compileJava --no-daemon
```
Se place dans le module de service et lance sa compilation Java avec le wrapper Gradle du dépôt. `--no-daemon` exécute Gradle sans conserver de processus daemon après le build.

## Création du commit consolidé et mise à jour de SR-25

```bash
git -C '<tmp>/sr25-squash' commit \
  -m 'feat(SR-25): consolidate SR-25 through SR-36' \
  -m 'Co-authored-by: Copilot <223556219+Copilot@users.noreply.github.com>'
```
Crée le commit unique à partir des changements indexés. Le premier `-m` définit le sujet; le second ajoute le trailer de co-auteur requis.

```bash
git update-ref -m 'squash SR-25 through SR-36' \
  refs/heads/working/SR-25 \
  fa6b647c8451a1cbb937e5d75c22740e9ec81e0a \
  41f784589144748a12a825c2148161feecb5f61d
```
Déplace la référence locale de `working/SR-25` vers le nouveau commit. Le dernier SHA est la valeur attendue avant modification : Git refuse l'opération si la branche a changé entre-temps. Cette mise à jour concerne uniquement le dépôt local.

```bash
git switch working/SR-25
```
Bascule le worktree principal sur la branche SR-25 mise à jour.

## Suppression des branches locales intermédiaires

```bash
git update-ref --stdin <<'EOF'
start
delete refs/heads/working/SR-26 473fbfcb718ccfc383a4a6d6836c314a2b6df2a6
delete refs/heads/working/SR-27 fb782f5b5805fe56a1c756b93204f68d23b8187c
delete refs/heads/working/SR-28 25c8bca57dd2e1dad3b44e9f18e4e84ec4d4b05b
delete refs/heads/working/SR-28-follow-up 0efa37c760a0c00613e48fa22397e4398a880b82
delete refs/heads/working/SR-29 0efa37c760a0c00613e48fa22397e4398a880b82
delete refs/heads/working/SR-30 d8fa5139f5414b8d3aaa293af3e8f8fb2779bddd
delete refs/heads/working/SR-31 243c67586468ffab0589a67612d8c479ac7b38f5
delete refs/heads/working/SR-32 4243a56c6ede0afbcbae90c310b8cb59fc68da1a
delete refs/heads/working/SR-33 b2999d7e9876984ded0f6e7e11ee23f4c347e1c2
delete refs/heads/working/SR-34 b2c1026c0d3e6a7b2d99dd70218160e64e374b92
delete refs/heads/working/SR-35 e57acd4376993510cb2d33fbb411f1d2fb5a1997
delete refs/heads/working/SR-36 14b7997d84ac693d9302ff8f6938b7c7b149906c
prepare
commit
EOF
```
Exécute une transaction Git atomique sur les références locales : `start` commence la transaction; chaque ligne `delete` supprime la branche indiquée uniquement si elle pointe encore sur le SHA fourni; `prepare` vérifie que la transaction peut être appliquée; `commit` valide toutes les suppressions ensemble. La transaction supprimait SR-26 à SR-36 ainsi que `working/SR-28-follow-up`, sans toucher aux branches antérieures à SR-25 ni aux références distantes.

## Nettoyage et vérification finale

```bash
git worktree remove --force '<tmp>/sr25-squash'
```
Supprime le worktree temporaire après la création et la validation du commit. `--force` était nécessaire car il contenait un sous-module initialisé; il ne s'agit pas du worktree principal.

```bash
test "$(git rev-parse 'stash@{0}')" = ac49d4c33ded7856bf33b7bc68e77f2f0d54fa61 && git stash drop 'stash@{0}'
```
Vérifie d'abord que le premier stash est bien la sauvegarde temporaire créée pour cette opération, puis la retire. Si le SHA ne correspond pas, `git stash drop` n'est pas exécuté. Le stash antérieur de l'utilisateur est ainsi préservé.

```bash
git status --short
```
Confirme qu'il ne reste pas de changements non commités dans le worktree principal.

```bash
git branch --list 'working/SR-*'
```
Affiche les branches locales dont le nom commence par `working/SR-`, afin de vérifier que SR-25 subsiste et que les branches intermédiaires ont disparu.

```bash
git log -1 --format='%H%n%P%n%s' working/SR-25
```
Affiche le SHA du commit SR-25, son ou ses parents, puis son sujet; permet de confirmer que le commit consolidé est basé sur `develop`.

```bash
git rev-list --count develop..working/SR-25
```
Compte les commits de SR-25 qui ne sont pas dans `develop`; le résultat attendu après squash est `1`.

```bash
git ls-remote --heads origin 'working/SR-*'
```
Vérifie une dernière fois les branches SR sur le remote sans effectuer de push ni modifier les références distantes.

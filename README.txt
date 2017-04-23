Le programme marche uniquement sous Linux et Windows
Pour inclure le projet eclipse il vous suffit juste de mettre le dossier Project_Fractal_V.4.0 dans votre Workspace et de créer ensuite un projet au même nom.

Les librairies sont normalement déjà incluses mais dans le cas où vous avez des problèmes avec opengl suivent les instructions ci-dessous.Inclure les librairies:
Sous eclipse clic droit sur le projet => Java Build path => Librairies Add External JARs
Les jars se trouvent dans le dossier lib (lwjgl2.9.3 et slick).
Pensez à inclure les native pour lwjgl-util.jar et lwjgl.jar après avoir ajouté les .jar en cliquant sur la flèche les natives ce trouvent dans le dossier lwjgl2.9.3
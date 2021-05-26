create schema leonard9_Grupo1Eq1;
use leonard9_Grupo1Eq1;

create table usuario(
id int not null auto_increment primary key,
nombre varchar(50),
apellido varchar(50),
nickname varchar(30),
email varchar(30),
telefono varchar(20),
contrasena varchar(255),
direccion varchar(80),
foto mediumblob default null
);

create table especie(
id int not null auto_increment primary key,
especie varchar(15)
);

create table publicacion(
id int not null auto_increment primary key,
descripcion text,
edad varchar(15),
detalles text,
fecha_hora timestamp default current_timestamp on update current_timestamp,
estado enum('borrador', 'eliminado', 'publicado', 'adoptado'),
id_especie int not null,
id_usuario int not null,
constraint FK_Publicacion_Usuario foreign key (id_usuario) references usuario(id),
constraint FK_Publicacion_Especie foreign key (id_especie) references especie(id)
);

create table comentario(
id int not null auto_increment primary key,
comentario text,
fecha_hora timestamp default current_timestamp on update current_timestamp,
id_usuario int not null,
id_publicacion int not null,
constraint FK_Comentario_Usuario foreign key (id_usuario) references usuario(id),
constraint FK_Comentario_Publicacion foreign key (id_publicacion) references publicacion(id)
);

create table foto(
id int not null auto_increment primary key,
foto mediumblob,
indice int,
id_publicacion int not null,
constraint FK_Foto_Publicacion foreign key (id_publicacion) references publicacion(id)
);

create table guardado(
id int not null auto_increment primary key,
id_publicacion int not null,
id_usuario int not null,
constraint FK_Guardado_Publicacion foreign key (id_publicacion) references publicacion(id),
constraint FK_Guardado_Usuario foreign key (id_usuario) references usuario(id)
);

insert into especie (especie) values ('Perros');
insert into especie (especie) values ('Gatos');
insert into especie (especie) values ('Peces');
insert into especie (especie) values ('Aves');
insert into especie (especie) values ('Hamsters');
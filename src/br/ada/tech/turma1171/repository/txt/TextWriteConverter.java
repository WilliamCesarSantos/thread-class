package br.ada.tech.turma1171.repository.txt;

public interface TextWriteConverter<T> {

    String write(T object);

}

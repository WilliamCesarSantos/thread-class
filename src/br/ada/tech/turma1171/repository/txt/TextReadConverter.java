package br.ada.tech.turma1171.repository.txt;

public interface TextReadConverter<T> {

    T read(String value);

}

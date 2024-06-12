package com.aluracursos.literalura.service;

public interface IDataConvertor {
    <T> T obtainData(String json, Class<T> clase);
}

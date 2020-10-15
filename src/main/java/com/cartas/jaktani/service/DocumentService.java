package com.cartas.jaktani.service;

import com.cartas.jaktani.dto.DocumentDto;

public interface DocumentService {
	Object findDocumentByID(DocumentDto documentDto);
    Object deleteDocumentByID(DocumentDto documentDto);
    Object addDocument(DocumentDto documentDto);
    Object editDocument(DocumentDto documentDto);
    Object findAllByType(DocumentDto documentDto);
    Object findAllByRefferenceIdAndType(DocumentDto documentDto);
    Object findAllByRefferenceIdAndTypeAndCode(DocumentDto documentDto);

}

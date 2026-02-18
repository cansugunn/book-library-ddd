package com.finalproject.infrastructure.common.fms;

import com.finalproject.application.ports.output.fms.FileStoragePort;

public class FileStoragePortDecorator implements FileStoragePort {
    private final LocalFileStorageAdapter localFileStorageAdapter;
    private final FileUrlResolver fileUrlResolver;

    public FileStoragePortDecorator(LocalFileStorageAdapter localFileStorageAdapter,
                                    FileUrlResolver fileUrlResolver) {
        this.localFileStorageAdapter = localFileStorageAdapter;
        this.fileUrlResolver = fileUrlResolver;
    }

    @Override
    public String resolvePublicUrl(String storageKey) {
        return fileUrlResolver.resolvePublicUrl(localFileStorageAdapter.toStorageKey(storageKey));
    }
}

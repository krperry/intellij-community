// Copyright 2000-2019 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.intellij.openapi.vcs.history;

import com.intellij.openapi.vcs.RepositoryLocation;
import com.intellij.util.ArrayUtilRt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;

public interface VcsFileRevision extends VcsFileContent, VcsRevisionDescription {
  VcsFileRevision NULL = new VcsFileRevision() {
    @Override
    @NotNull
    public VcsRevisionNumber getRevisionNumber() {
      return VcsRevisionNumber.NULL;
    }

    @Override
    public Date getRevisionDate() {
      return new Date();
    }

    @Override
    public String getAuthor() {
      return "";
    }

    @Override
    public String getCommitMessage() {
      return "";
    }

    @Override
    public String getBranchName() {
      return null;
    }

    @Nullable
    @Override
    public RepositoryLocation getChangedRepositoryPath() {
      return null;
    }

    @Override
    public byte[] getContent() {
      return loadContent();
    }

    @Override
    public byte @NotNull [] loadContent() {
      return ArrayUtilRt.EMPTY_BYTE_ARRAY;
    }
  };

  @Nullable
  String getBranchName();

  @Nullable
  RepositoryLocation getChangedRepositoryPath();
}

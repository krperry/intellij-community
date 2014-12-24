package com.intellij.openapi.vcs.changes.actions.diff;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypes;
import com.intellij.openapi.fileTypes.PlainTextFileType;
import com.intellij.openapi.fileTypes.UnknownFileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.diff.contents.DiffContent;
import com.intellij.openapi.util.diff.contents.DocumentContentImpl;
import com.intellij.openapi.util.diff.impl.DiffContentFactory;
import com.intellij.openapi.vcs.FilePath;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.LineSeparator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.charset.Charset;

public class FileAwareDocumentContent extends DocumentContentImpl {
  @NotNull private final Project myProject;
  @Nullable private final VirtualFile myLocalFile;

  public FileAwareDocumentContent(@NotNull Project project,
                                  @NotNull Document document,
                                  @Nullable FileType fileType,
                                  @Nullable VirtualFile localFile,
                                  @Nullable LineSeparator separator,
                                  @Nullable Charset charset) {
    super(document, fileType, localFile, separator, charset);
    myProject = project;
    myLocalFile = localFile;
  }

  public OpenFileDescriptor getOpenFileDescriptor(int offset) {
    return myLocalFile == null ? null : new OpenFileDescriptor(myProject, myLocalFile, offset);
  }

  @NotNull
  public static DiffContent create(@NotNull Project project,
                                   @NotNull String content,
                                   @NotNull FilePath path) {
    VirtualFile localFile = LocalFileSystem.getInstance().findFileByPath(path.getPath());
    FileType fileType = localFile != null ? localFile.getFileType() : path.getFileType();
    Charset charset = localFile != null ? localFile.getCharset() : path.getCharset(project);
    return create(project, content, fileType, localFile, charset);
  }

  @NotNull
  public static DiffContent create(@NotNull Project project,
                                   @NotNull String content,
                                   @NotNull VirtualFile file) {
    FileType fileType = file.getFileType();
    Charset charset = file.getCharset();
    return create(project, content, fileType, file, charset);
  }

  @NotNull
  private static DiffContent create(@NotNull Project project,
                                    @NotNull String content,
                                    @Nullable FileType fileType,
                                    @Nullable VirtualFile file,
                                    @Nullable Charset charset) {
    Pair<Document, LineSeparator> pair = DiffContentFactory.buildDocument(content);
    pair.first.setReadOnly(true);
    if (FileTypes.UNKNOWN.equals(fileType)) fileType = PlainTextFileType.INSTANCE;
    return new FileAwareDocumentContent(project, pair.first, fileType, file, pair.second, charset);
  }
}

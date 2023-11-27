package com.ashlixx.stackoverflowplugin;

import com.intellij.ide.BrowserUtil;
import com.intellij.lang.Language;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiFile;

public class SearchAction extends AnAction
{
    /**
     * Convert selected text to a URL friendly string.
     * @param event
     */
    @Override
    public void actionPerformed(AnActionEvent event) {
        final Editor editor = event.getRequiredData(CommonDataKeys.EDITOR);
        CaretModel caretModel = editor.getCaretModel();

        // For searches from the editor, we should get file type information
        // to add scope to the search using the StackOverflow search syntax.

        String languageTag = "";
        PsiFile file = event.getData(CommonDataKeys.PSI_FILE);
        if(file != null)
        {
            Language lang = event.getData(CommonDataKeys.PSI_FILE).getLanguage();
            languageTag = "+[" + lang.getDisplayName().toLowerCase() + "]";
        }

        // The update method below is only called periodically so need
        // to be careful to check for selected text
        if(caretModel.getCurrentCaret().hasSelection())
        {
            String query = caretModel.getCurrentCaret().getSelectedText().replace(' ', '+') + languageTag;
            BrowserUtil.browse("https://stackoverflow.com/search?q=" + query);
        }
    }

    /**
     * Just make this action visible when text is selected.
     * @param event
     */
    @Override
    public void update(AnActionEvent event) {
        final Editor editor = event.getRequiredData(CommonDataKeys.EDITOR);
        CaretModel caretModel = editor.getCaretModel();
        event.getPresentation().setEnabledAndVisible(caretModel.getCurrentCaret().hasSelection());
    }

    @Override
    public boolean isDumbAware() {
        return false;
    }
}
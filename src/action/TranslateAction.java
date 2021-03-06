package action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import common.SupportLanguage;
import dialog.ChooseLanguageDialog;
import task.BackgroundTask;

import java.util.List;


public class TranslateAction extends AnAction {

    private VirtualFile chooseFile;
    private ChooseLanguageDialog chooseLanguageDialog;
    private Project project;


    /**
     * 这个方法用于点击后的实现
     * @param e
     */
    @Override
    public void actionPerformed(AnActionEvent e) {
        // TODO: insert action logic here
        chooseFile = CommonDataKeys.VIRTUAL_FILE.getData(e.getDataContext());
//        System.out.println(chooseFile.getPath());
        project = CommonDataKeys.PROJECT.getData(e.getDataContext());

        chooseLanguageDialog = new ChooseLanguageDialog(project, "Translating strings.xml", false);
        chooseLanguageDialog.setiConfirmListener(new ChooseLanguageDialog.IConfirmListener() {
            @Override
            public void confirm(List<SupportLanguage.LanguageType> selectedLanguageList, boolean isDefaultChinese) {
                operateStringXmlFile(selectedLanguageList , isDefaultChinese);
                chooseLanguageDialog.close(-1);
            }
        });

        chooseLanguageDialog.show();

    }

    /**
     * 操作strings.xml文件
     *
     * @param selectedLanguageList
     * @param isDefaultChinese
     */
    private void operateStringXmlFile(List<SupportLanguage.LanguageType> selectedLanguageList, boolean isDefaultChinese) {
        new BackgroundTask(project, "Writing to xml", chooseFile , selectedLanguageList , isDefaultChinese).queue();
    }

    /**
     * 这个方法先于点击
     * @param e
     */
    @Override
    public void update(AnActionEvent e) {
        super.update(e);

        adjustStringsXmlFile(e);

    }

    /**
     * 判断是否为strings.xml文件
     * @param e
     */
    private void adjustStringsXmlFile(AnActionEvent e) {
        chooseFile = CommonDataKeys.VIRTUAL_FILE.getData(e.getDataContext());
        if (!chooseFile.getName().equals("strings.xml")) {
            e.getPresentation().setEnabled(false);
            e.getPresentation().setVisible(false);
        }
    }
}

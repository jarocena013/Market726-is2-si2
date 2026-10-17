package gui;


import java.awt.Color;
import java.awt.Font;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

public class LanguagePanel extends JPanel {

    public LanguagePanel(Runnable refreshAction) {
        JLabel label = new JLabel(
            ResourceBundle.getBundle("Etiquetas")
                .getString("MainGUI.SelectOption")
        );

        label.setFont(new Font("Tahoma", Font.BOLD, 13));
        label.setForeground(Color.BLACK);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        add(label);

        ButtonGroup buttonGroup = new ButtonGroup();

        addLanguageButton(buttonGroup, "English", "en", refreshAction);
        addLanguageButton(buttonGroup, "Euskara", "eus", refreshAction);
        addLanguageButton(buttonGroup, "Castellano", "es", refreshAction);
    }

    private void addLanguageButton(
            ButtonGroup group,
            String text,
            String language,
            Runnable refreshAction) {

        JRadioButton button = new JRadioButton(text);

        button.addActionListener(e -> {
            Locale.setDefault(new Locale(language));
            refreshAction.run();
        });

        group.add(button);
        add(button);
    }
}


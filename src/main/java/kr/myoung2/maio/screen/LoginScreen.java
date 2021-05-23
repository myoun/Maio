package kr.myoung2.maio.screen;

import com.mojang.authlib.exceptions.AuthenticationException;
import kr.myoung2.maio.screen.widget.PasswordFieldWidget;
import kr.myoung2.maio.util.AuthUtils;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TranslatableText;

public class LoginScreen extends Screen {

    private final Screen parentScreen;

    private TextFieldWidget emailField, passwordField;
    private ButtonWidget loginButton, cancelButton;

    public LoginScreen(Screen parentScreen) {
        super(new TranslatableText("maio.screen.login"));
        this.parentScreen = parentScreen;
    }

    @Override
    protected void init() {
        super.init();
        this.client.keyboard.setRepeatEvents(true);

        emailField = new TextFieldWidget(this.client.textRenderer,
                width/2-100,
                76,
                200,
                20,
                new TranslatableText("maio.screen.login.email"));
        emailField.setMaxLength(128);
        emailField.setSuggestion(this.client.getSession().getUsername()+"@email.com");
        emailField.setChangedListener(value -> {
            emailField.setSuggestion(value.isEmpty() ? this.client.getSession().getUsername()+"@email.com" : "");
            loginButton.active = canSubmit();
        });
        children.add(emailField);
        

        // Password Field

        passwordField = new PasswordFieldWidget(this.client.textRenderer,
                width / 2 - 100,
                116,
                200,
                20,
                new TranslatableText("maio.screen.login.password"));

        passwordField.setChangedListener(value -> {
            loginButton.setMessage(new TranslatableText("maio.screen.login.button"));
            loginButton.active = canSubmit();
            cancelButton.setMessage(new TranslatableText("maio.screen.login.button.cancel"));
        });
        children.add(passwordField);
        loginButton = new ButtonWidget(width / 2 - 100,
                height / 4 + 96 + 18,
                200,
                20,
                new TranslatableText("maio.screen.login.button"),
                button -> submit());
        loginButton.active = false;
        addButton(loginButton);
        cancelButton = new ButtonWidget(width / 2 - 100,
                height / 4 + 120 + 18,
                200,
                20,
                new TranslatableText("maio.screen.login.button.cancel"),
                button -> onClose());
        addButton(cancelButton);
    }

    @Override
    public boolean shouldCloseOnEsc()
    {
        return !emailField.isFocused() && !passwordField.isFocused();
    }

    @Override
    public void onClose()
    {
        passwordField.setText("");
        this.client.openScreen(parentScreen);
    }

    @Override
    public void removed()
    {
        this.client.keyboard.setRepeatEvents(false);
    }



    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta)
    {

        renderBackground(matrices);

        drawCenteredText(matrices, this.client.textRenderer, title, width / 2, 17, 16777215);

        drawTextWithShadow(matrices,
                this.client.textRenderer,
                new TranslatableText("maio.screen.login.field.email"),
                width / 2 - 100,
                64,
                10526880);
        drawTextWithShadow(matrices,
                this.client.textRenderer,
                new TranslatableText("maio.screen.login.field.password"),
                width / 2 - 100,
                104,
                10526880);

        emailField.render(matrices, mouseX, mouseY, delta);
        passwordField.render(matrices, mouseX, mouseY, delta);

        super.render(matrices, mouseX, mouseY, delta);
    }

    protected boolean canSubmit()
    {
        return !emailField.getText().isEmpty() || !passwordField.getText().isEmpty();
    }


    public void submit()
    {
        String email = emailField.getText();
        String password=  passwordField.getText();
        System.out.println(email);
        System.out.println(password);
        if (email != "" && password == "") {
            AuthUtils.offlineLogin(email);
            onClose();
            return;
        }
        try {
            AuthUtils.login(email,password);
        } catch (AuthenticationException e) {
            e.printStackTrace();
            onClose();
            return;
        }
        onClose();
    }
}

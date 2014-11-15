


public class Register {

	private Client c;

	public Register(Client c) {
		this.c = c;
		password = "";
		confirmPass = "";
		username = "";
		referrer = "";
		email = "";
		confirmEmail = "";
		
	}

	/**
	 * The Username to register
	 * 
	 * @return
	 */
	public String getName() {
		return username == "" ? null : TextClass.fixName(username);
	}

	/**
	 * The Password to register
	 * 
	 * @return
	 */
	public String getPass() {
		return password == "" ? null : password;
	}

	/**
	 * The Password confirmation
	 * 
	 * @return
	 */
	public String getConfirmPass() {
		return confirmPass == "" ? null : confirmPass;
	}

	/**
	 * The email to register
	 * 
	 * @return
	 */
	public String getEmail() {
		return email == "" ? null : email;
	}
	/**
	 * The email to register
	 * 
	 * @return
	 */
	public String getConfirmEmail() {
		return confirmEmail == "" ? null : confirmEmail;
	}
	/**
	 * The player referrer (optional)
	 * 
	 * @return
	 */
	public String getReferrer() {
		return referrer == "" ? null : TextClass.fixName(referrer);
	}

	/**
	 * Gets the messages based on type
	 * 
	 * @param type
	 * @return
	 */
	public String[] getInfoMessage(int type) {
		switch (type) {
		case 0:
			return new String[] { "Your username must consist of alphanumeric", "characters and spaces, and must not exceed", "12 characters." };
		case 1:
			return new String[] { "Your password must consist of alphanumeric", "characters, must be at least 5 characters", "and must not exceed 20 characters." };
		case 2:
			return new String[] { "Confirm your password by retyping it." };
		case 3:
			return new String[] { "If you do not enter a valid email address,", "you will not be able to recover your account", "if your password is changed or forgotten." };
		case 4:
			return new String[] {"Confirm your E-mail by retyping it."};
		case 5:
			return new String[] { "A referrer is not required, but if you", "wish to benefit the person referring you", "please enter his name." };

		}
		return null;
	}

	/**
	 * Gets the error message based on type
	 * 
	 * @param type
	 * @return
	 */
	public String[] getErrorMessage(int type) {
		switch (type) {
		case 0:
			return new String[] { "Username contains invalid characters." };
		case 1:
			return new String[] { "Your password cannot be your username!" };
		case 2:
			return new String[] { "Your password must be at least", "5 characters long." };
		case 3:
			return new String[] { "This must match the previously", "typed password." };
		case 4:
			return new String[] { "This is not a valid email address." };
		case 5:
			return new String[] { "You can't refer yourself!" };
		case 6:
			return new String[] { "Referrer name contains invalid characters." };
		case 7:
			return new String[] {"This must match the previously", "typed e-mail address"};
		}
		return null;
	}

	public String[] getMessageForPath(int path) {
		switch (path) {
		case 0:
			return usernameMessage;
		case 1:
			return passwordMessage;
		case 2:
			return confirmPassMessage;
		case 3:
			return emailMessage;
		case 4:
			return confirmEmailMessage;
		case 5:
			return referrerMessage;
		}
		return null;
	}

	/**
	 * Checks if the username entered is valid
	 * 
	 * @return
	 */
	public boolean validUsername() {
		if (getName() == null) {
			return false;
		}
		if (getName().length() > 0) {
			if (!TextClass.isValidName(getName())) {
				usernameMessage = getErrorMessage(0);
			}
			return TextClass.isValidName(getName());
		}
		return false;
	}

	/**
	 * Checks if the password entered is valid
	 * 
	 * @return
	 */
	public boolean validPassword() {
		if (getPass() == null) {
			return false;
		}
		if (getPass().equalsIgnoreCase(getName())) {
			passwordMessage = getErrorMessage(1);
			return false;
		}
		if (getPass().length() < 5) {
			passwordMessage = getErrorMessage(2);
			return false;
		}
		return TextClass.isAlphanumeric(getPass());
	}

	/**
	 * Checks if the player gives a valid password confirmation
	 * 
	 * @return
	 */
	public boolean validConfirmation() {
		if (getPass() == null) {
			return false;
		}
		if(confirmPass == null)
			return false;
		if (getPass().length() > 0 && confirmPass.length() > 0) {
			if (getPass().equalsIgnoreCase(confirmPass)) {
				return true;
			} else {
				confirmPassMessage = getErrorMessage(3);
				return false;
			}
		}
		return false;
	}

	/**
	 * Checks if a player gives a valid email
	 * 
	 * @return
	 */
	public boolean validEmail() {
		if (getEmail() == null) {
			return false;
		}
		if (getEmail().length() == 0) {
			return false;
		}
		if (!TextClass.isValidEmail(getEmail())) {
			emailMessage = getErrorMessage(4);
		}
		return TextClass.isValidEmail(getEmail());
	}
	public boolean validConfirmEmail()
	{
		if(getConfirmEmail() == null)
			return false;
		if(getEmail() == null)
			return false;
		if(getEmail().equals(getConfirmEmail()))
			return true;
		return false;
	}

	/**
	 * Checks if the referrer username is valid
	 * 
	 * @return
	 */
	public boolean validReferrer() {
		if (getReferrer() == null) {
			return false;
		}
		if (getReferrer().length() == 0) {
			return true;
		}
		if (getName().equalsIgnoreCase(getReferrer())) {
			referrerMessage = getErrorMessage(5);
			return false;
		}
		if (getReferrer().length() > 0) {
			if (!TextClass.isValidName(getReferrer())) {
				referrerMessage = getErrorMessage(6);
			}
			return TextClass.isValidName(getReferrer());
		}
		return false;
	}

	/**
	 * Checks if the player is ready to proceed to the next step(create the
	 * account)
	 * 
	 * @return
	 */
	public boolean canCreate() {
		return validUsername() && validPassword() && validConfirmation() && validEmail() && validConfirmEmail();
	}


	public void render() {

		int baseX = (Client.clientWidth / 2) - (SpriteCache.spriteCache[36].myWidth / 2);
		int baseY = (Client.clientHeight / 2) - (SpriteCache.spriteCache[36].myHeight / 2);

		SpriteCache.get(756).drawAdvancedSprite(baseX + ((SpriteCache.get(694).myWidth / 2) - (SpriteCache.get(756).myWidth / 2)),
				baseY + ((SpriteCache.get(694).myHeight / 2) - (SpriteCache.get(756).myHeight / 2)));
		
		
	}

	public void processInput() {
		try {
		for (int i = 0; i < coordsY.length; i++) {
			if (c.saveClickX >= Client.clientWidth / 2 - 137+135 && c.saveClickX <= Client.clientWidth / 2 + 57+165 && c.saveClickY >= Client.clientHeight / 2 - coordsY[i][0]+20 && c.saveClickY <= Client.clientHeight / 2 - coordsY[i][1]+9) {
				c.loginScreenCursorPos = 2 + i;
			}
		}

		int baseX = (Client.clientWidth / 2) - (SpriteCache.spriteCache[694].myWidth / 2);
		int baseY = (Client.clientHeight / 2) - (SpriteCache.spriteCache[694].myHeight / 2);

		if (c.clickMode3 == 1 && (c.saveClickX >= baseX+545 && c.saveClickX <= baseX+599 && c.saveClickY >= baseY+493 && c.saveClickY <= baseY+539)) {
			c.loginScreenState = 1;
			username = "";
			password = "";
			confirmPass = "";
			email = "";
			confirmEmail = "";
			referrer = "";
		}

		if (c.clickMode3 == 1 && (c.saveClickX >= baseX+283 && c.saveClickX <= baseX + 462 && c.saveClickY >= baseY+493 && c.saveClickY <= baseY+539)) {
			if (canCreate()) {
				c.myUsername = getName();
				c.myPassword = getPass();
				c.previousScreenState = 3;
				c.loginScreenState = 1;
			}
		}
		verified[1] = validPassword();
		verified[2] = validConfirmation();
		do {
			int keyPressed = c.readChar(-796);
			if (keyPressed == -1)
				break;
			boolean validKey = false;
			for (int i2 = 0; i2 < Client.validUserPassChars.length(); i2++) {
				if (keyPressed != Client.validUserPassChars.charAt(i2))
					continue;
				validKey = true;
				break;
			}
			verified[0] = validUsername();
			verified[3] = validEmail();
			verified[5] = validReferrer();
			if (c.loginScreenCursorPos == 2) {
				if (keyPressed == 8 && username.length() > 0)
					username = username.substring(0, username.length() - 1);
				if (keyPressed == 9 || keyPressed == 10 || keyPressed == 13)
					c.loginScreenCursorPos = 3;

				if (validKey)
					username += (char) keyPressed;
				if (username.length() > 12)
					username = username.substring(0, 12);
			} else if (c.loginScreenCursorPos == 3) {
				if (keyPressed == 8 && password.length() > 0)
					password = password.substring(0, password.length() - 1);
				if (keyPressed == 9 || keyPressed == 10 || keyPressed == 13)
					c.loginScreenCursorPos = 4;

				if (validKey)
					password += (char) keyPressed;
				if (password.length() > 20)
					password = password.substring(0, 20);
			} else if (c.loginScreenCursorPos == 4) {
				if (keyPressed == 8 && confirmPass.length() > 0)
					confirmPass = confirmPass.substring(0, confirmPass.length() - 1);
				if (keyPressed == 9 || keyPressed == 10 || keyPressed == 13)
					c.loginScreenCursorPos = 5;

				if (validKey)
					confirmPass += (char) keyPressed;
				if (confirmPass.length() > 20)
					confirmPass = confirmPass.substring(0, 20);
			} else if (c.loginScreenCursorPos == 5) {
				if (keyPressed == 8 && email.length() > 0)
					email = email.substring(0, email.length() - 1);
				if (keyPressed == 9 || keyPressed == 10 || keyPressed == 13)
					c.loginScreenCursorPos = 6;

				if (validKey)
					email += (char) keyPressed;
				if (email.length() > 34)
					email = email.substring(0, 34);
			} else if (c.loginScreenCursorPos == 6) {
				if (keyPressed == 8 && confirmEmail.length() > 0)
					confirmEmail = confirmEmail.substring(0, confirmEmail.length() - 1);
				if (keyPressed == 9 || keyPressed == 10 || keyPressed == 13)
					c.loginScreenCursorPos = 7;

				if (validKey)
					confirmEmail += (char) keyPressed;
				if (confirmEmail.length() > 34)
					confirmEmail = confirmEmail.substring(0, 34);

			} else if (c.loginScreenCursorPos == 7) {
				if (keyPressed == 8 && referrer.length() > 0)
					referrer = referrer.substring(0, referrer.length() - 1);
				if (keyPressed == 9 || keyPressed == 10 || keyPressed == 13)
					c.loginScreenCursorPos = 2;

				if (validKey)
					referrer += (char) keyPressed;
				if (referrer.length() > 12)
					referrer = referrer.substring(0, 12);
			}
			verified[4] = validConfirmEmail();
		} while (true);
		return;
		} catch(Exception e)
		{
			
		}
	}

	public String[] usernameMessage = { "This field is valid." };
	public String[] passwordMessage = { "This field is valid." };
	public String[] confirmPassMessage = { "This field is valid." };
	public String[] emailMessage = { "This field is valid." };
	public String[] referrerMessage = { "This field is valid." };
	public String[] confirmEmailMessage = {"This field is not valid."};
	public String username = "";
	public String password = "";
	public String confirmPass = "";
	public String confirmEmail = "";
	public String email = "";
	public String referrer = "";//hoger is lager
	private final int[][] coordsY = { 
			{ 126-62, 89-62}, 
			{ 72 - 43, 32 - 43}, 
			{ 13-23, -28-23 },
			{ -45-9, -83-9}, 
			{-103-97+105, -139-97+105},
			{ -103-97+65, -139-97+64 } };
	private final int[] coordsY2 = { 108-13-59, 50-51, -6-36, -62-21, -120-4, -124-40 };
	private final int[] coordsY3 = { 95-69, 38-50, -22-29, -75-17, -131 , -131-42};
	public boolean[] verified = { validUsername(), validPassword(), validConfirmation(), validEmail(), validConfirmEmail(), validReferrer() };
}
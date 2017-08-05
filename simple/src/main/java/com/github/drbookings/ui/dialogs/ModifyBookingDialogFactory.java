package com.github.drbookings.ui.dialogs;

import com.github.drbookings.model.data.manager.MainManager;
import com.github.drbookings.ui.controller.ModifyBookingController;

public class ModifyBookingDialogFactory extends AbstractViewFactory {

	private final MainManager manager;

	public ModifyBookingDialogFactory(final MainManager manager) {
		this.manager = manager;
		setTitle("Modify Booking");
		setFxml("/fxml/ModifyBookingView.fxml");
		setWidth(250);
		setHeight(350);
	}

	@Override
	protected void visitController(final Object controller) {
		final ModifyBookingController c = (ModifyBookingController) controller;
		c.setManager(manager);

	}

}

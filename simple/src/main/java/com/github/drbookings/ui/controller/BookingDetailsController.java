package com.github.drbookings.ui.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import com.github.drbookings.LocalDates;
import com.github.drbookings.model.data.Booking;
import com.github.drbookings.model.data.manager.MainManager;
import com.github.drbookings.model.settings.SettingsManager;
import com.github.drbookings.ui.CellSelectionManager;
import com.github.drbookings.ui.Styles;
import com.github.drbookings.ui.beans.RoomBean;
import com.github.drbookings.ui.dialogs.ModifyBookingDialogFactory;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class BookingDetailsController implements Initializable {

    public MainManager getManager() {
	return manager;
    }

    public void setManager(final MainManager manager) {
	this.manager = manager;
    }

    private static void addDates(final HBox content, final Booking be) {
	final TextFlow checkIn = LocalDates.getDateText(be.getCheckIn());
	final TextFlow checkOut = LocalDates.getDateText(be.getCheckOut());
	final TextFlow year = LocalDates.getYearText(be.getCheckOut());
	final TextFlow tf = new TextFlow();
	tf.getChildren().addAll(checkIn, new Text(" - "), checkOut, new Text("\n"), year);
	// tf.getChildren().addAll(checkIn, new Text(" ➤ "), checkOut);
	// HBox.setHgrow(tf, Priority.SOMETIMES);
	content.getChildren().add(tf);

    }

    private static void addName(final HBox content, final Booking be) {
	final Label label = new Label(be.getGuest().getName() + "\n" + be.getBookingOrigin().getName());
	label.setWrapText(true);
	content.getStyleClass().add(Styles.getBackgroundStyleSource(be.getBookingOrigin().getName()));
	content.getChildren().add(label);
	HBox.setHgrow(label, Priority.ALWAYS);

    }

    private static void addNights(final HBox content, final Booking be) {
	final Text label = new Text(be.getNumberOfNights() + " nights");
	content.getChildren().add(label);
	// HBox.setHgrow(label, Priority.SOMETIMES);
    }

    private final ListChangeListener<RoomBean> roomListener = c -> Platform.runLater(() -> update(c.getList()));

    @FXML
    private VBox content;

    private final Map<Booking, TextInputControl> booking2CheckInNote = new HashMap<>();

    private final Map<Booking, TextInputControl> booking2CheckOutNote = new HashMap<>();

    private final Map<Booking, TextInputControl> booking2SpecialRequestNote = new HashMap<>();

    private final Map<Booking, TextInputControl> booking2GrossEarnings = new HashMap<>();

    private final Map<Booking, CheckBox> booking2WelcomeMail = new HashMap<>();

    private final Map<Booking, CheckBox> booking2Payment = new HashMap<>();

    private MainManager manager;

    private void addCheckInNote(final Pane content, final Booking be) {
	final VBox b = new VBox();
	b.getChildren().add(new Text("Check-in Note"));
	final TextArea ta0 = new TextArea(be.getCheckInNote());
	ta0.setWrapText(true);
	ta0.setPrefHeight(80);
	b.getChildren().add(ta0);
	booking2CheckInNote.put(be, ta0);
	content.getChildren().add(b);

    }

    private void addCheckOutNote(final Pane content, final Booking be) {
	final VBox b = new VBox();
	b.getChildren().add(new Text("Check-out Note"));
	final TextArea ta0 = new TextArea(be.getCheckOutNote());
	ta0.setWrapText(true);
	ta0.setPrefHeight(80);
	b.getChildren().add(ta0);
	booking2CheckOutNote.put(be, ta0);
	content.getChildren().add(b);

    }

    private static void addRow0(final Pane content, final Booking be) {
	final HBox box = new HBox(8);
	box.setPadding(new Insets(4));
	box.setAlignment(Pos.CENTER);
	box.setFillHeight(true);
	addName(box, be);
	box.getChildren().add(new Separator(Orientation.VERTICAL));
	addDates(box, be);
	box.getChildren().add(new Separator(Orientation.VERTICAL));
	addNights(box, be);
	box.getStyleClass().add("first-line");
	content.getChildren().add(box);

    }

    private void addRow1(final Pane content, final Booking be) {
	final VBox box = new VBox();
	final HBox box0 = new HBox();
	final HBox box1 = new HBox();
	final HBox box2 = new HBox();
	box.setFillWidth(true);
	box0.setFillHeight(true);
	box1.setFillHeight(true);
	box2.setFillHeight(true);
	addCheckInNote(box0, be);
	addCheckOutNote(box1, be);
	addSpecialRequestNote(box2, be);
	box.getChildren().addAll(box0, box1, box2);
	final TitledPane pane = new TitledPane("Notes", box);
	pane.setExpanded(false);
	content.getChildren().add(pane);

    }

    private static void addRow2(final Pane content, final Booking be) {

    }

    private void addRow3(final Pane content, final Booking be) {
	final HBox box = new HBox();
	box.setPadding(new Insets(4));
	box.setAlignment(Pos.CENTER_LEFT);
	box.setFillHeight(true);
	final TextField grossEarningsExpression = new TextField(be.getGrossEarningsExpression());
	grossEarningsExpression.setPrefWidth(120);
	booking2GrossEarnings.put(be, grossEarningsExpression);
	final Text grossEarnings = new Text(String.format("%3.2f", be.getGrossEarnings()));
	final TextFlow tf = new TextFlow(new Text("Gross Earnings: "), grossEarningsExpression, new Text(" = "),
		grossEarnings, new Label("€"));
	box.getChildren().addAll(tf);
	if (be.getGrossEarnings() <= 0) {
	    box.getStyleClass().add("warning");
	}
	content.getChildren().add(box);

    }

    private static void addRow4(final Pane content, final Booking be) {
	final HBox box = new HBox();
	box.setPadding(new Insets(4));
	box.setAlignment(Pos.CENTER_LEFT);
	box.setFillHeight(true);
	final TextFlow tf = new TextFlow();
	final Text t0 = new Text("Net Earnings: \t");
	final Text netEarnings = new Text(String.format("%3.2f", be.getNetEarnings()));
	final Text t1 = new Text("€ total \t");
	final Text netEarningsDay = new Text(String.format("%3.2f", be.getNetEarnings() / be.getNumberOfNights()));
	final Text t2 = new Text("€ /night");
	tf.getChildren().addAll(t0, netEarnings, t1, netEarningsDay, t2);
	box.getChildren().addAll(tf);
	if (be.getNetEarnings() <= 0) {
	    box.getStyleClass().add("warning");
	}
	content.getChildren().add(box);

    }

    private void addRow5(final Pane content, final Booking be) {
	final HBox box = new HBox();
	box.setPadding(new Insets(4));
	box.setFillHeight(true);
	final Text t0 = new Text("Welcome Mail sent: ");
	final CheckBox cb0 = new CheckBox();
	cb0.setSelected(be.isWelcomeMailSend());
	booking2WelcomeMail.put(be, cb0);
	final Text t1 = new Text(" \tPayment done: ");
	final CheckBox cb1 = new CheckBox();
	cb1.setSelected(be.isPaymentDone());
	booking2Payment.put(be, cb1);
	final TextFlow tf = new TextFlow();
	tf.getChildren().addAll(t0, cb0, t1, cb1);
	box.getChildren().add(tf);
	if (!be.isWelcomeMailSend()) {
	    box.getStyleClass().add("warning");
	}
	if (!be.isPaymentDone()) {
	    box.getStyleClass().add("warning");
	}
	content.getChildren().add(box);

    }

    private void addSeparator() {
	final HBox bb = new HBox();
	bb.setPrefHeight(20);
	bb.setAlignment(Pos.CENTER);
	final Separator s = new Separator();
	s.getStyleClass().add("large-separator");
	bb.getChildren().add(s);
	HBox.setHgrow(s, Priority.ALWAYS);
	content.getChildren().add(bb);
    }

    private void addSpecialRequestNote(final Pane content, final Booking be) {
	final VBox b = new VBox();
	b.getChildren().add(new Text("Special Requests"));
	final TextArea ta0 = new TextArea(be.getSpecialRequestNote());
	ta0.setWrapText(true);
	ta0.setPrefHeight(80);
	b.getChildren().add(ta0);
	booking2SpecialRequestNote.put(be, ta0);
	content.getChildren().add(b);

    }

    private void clearAll() {
	booking2CheckInNote.clear();
	booking2CheckOutNote.clear();
	booking2SpecialRequestNote.clear();
	booking2GrossEarnings.clear();
	booking2Payment.clear();
	booking2WelcomeMail.clear();
	content.getChildren().clear();
    }

    private ModifyBookingDialogFactory modifyBookingDialogFactory;

    @FXML
    public void handleActionSaveBookingDetails(final ActionEvent e) {
	for (final Entry<Booking, TextInputControl> en : booking2CheckInNote.entrySet()) {
	    en.getKey().setCheckInNote(en.getValue().getText());
	}
	for (final Entry<Booking, TextInputControl> en : booking2CheckOutNote.entrySet()) {
	    en.getKey().setCheckOutNote(en.getValue().getText());
	}
	for (final Entry<Booking, TextInputControl> en : booking2SpecialRequestNote.entrySet()) {
	    en.getKey().setSpecialRequestNote(en.getValue().getText());
	}
	for (final Entry<Booking, TextInputControl> en : booking2GrossEarnings.entrySet()) {
	    en.getKey().setGrossEarningsExpression(en.getValue().getText());
	}
	for (final Entry<Booking, CheckBox> en : booking2Payment.entrySet()) {
	    en.getKey().setPaymentDone(en.getValue().isSelected());
	}
	for (final Entry<Booking, CheckBox> en : booking2WelcomeMail.entrySet()) {
	    en.getKey().setWelcomeMailSend(en.getValue().isSelected());
	}
	// final Stage stage = (Stage) content.getScene().getWindow();
	// stage.close();
    }

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
	CellSelectionManager.getInstance().getSelection().addListener(roomListener);
	SettingsManager.getInstance().cleaningFeesProperty().addListener((ChangeListener<Number>) (observable, oldValue,
		newValue) -> update(CellSelectionManager.getInstance().getSelection()));
	update(CellSelectionManager.getInstance().getSelection());

    }

    private void update(final ObservableList<? extends RoomBean> rooms) {
	clearAll();
	final List<Booking> bookings = new ArrayList<>(rooms.stream()
		.flatMap(r -> r.getBookingEntries().stream().map(b -> b.getElement())).collect(Collectors.toSet()));
	Collections.sort(bookings);
	for (final Iterator<Booking> it = bookings.iterator(); it.hasNext();) {
	    final Booking be = it.next();
	    addBookingEntry(be);
	    if (it.hasNext()) {
		addSeparator();
	    }
	}
    }

    private void addBookingEntry(final Booking be) {
	final VBox box = new VBox(4);
	// box.setPadding(new Insets(4));
	addRow0(box, be);
	box.getChildren().add(new Separator());
	addRow3(box, be);
	box.getChildren().add(new Separator());
	addRow4(box, be);
	box.getChildren().add(new Separator());
	addRow5(box, be);
	box.getChildren().add(new Separator());
	addRow1(box, be);
	addRow2(box, be);
	addModifyButton(box, be);
	content.getChildren().add(box);

    }

    private void addModifyButton(final VBox box, final Booking be) {
	final Button b = new Button();
	b.setText("Modify");
	b.setPrefWidth(100);
	b.setOnAction(e -> {
	    if (modifyBookingDialogFactory == null) {
		modifyBookingDialogFactory = new ModifyBookingDialogFactory(getManager());
	    }
	    modifyBookingDialogFactory.showDialog();
	});
	box.getChildren().add(b);

    }

    public void shutDown() {

    }

}
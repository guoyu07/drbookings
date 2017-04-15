package com.github.drbookings.ui.controller;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.github.drbookings.model.data.Cleaning;
import com.github.drbookings.model.data.Room;

public class CleaningEntry extends DateRoomEntry<Cleaning> implements Comparable<CleaningEntry> {

    public CleaningEntry(final LocalDate date, final Room room, final Cleaning element) {
	super(date, room, element);
    }

    public static List<String> roomNameView(final Collection<CleaningEntry> e) {
	return e.stream().map(c -> c.getRoom().getName()).collect(Collectors.toList());

    }

    @Override
    public int compareTo(final CleaningEntry o) {
	return getDate().compareTo(o.getDate());
    }

}
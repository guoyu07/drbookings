package com.github.drbookings.model;

/*-
 * #%L
 * DrBookings
 * %%
 * Copyright (C) 2016 - 2017 Alexander Kerner
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 2 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-2.0.html>.
 * #L%
 */

import javafx.beans.property.ReadOnlyProperty;

/**
 * A general provider for gross earnings.
 *
 * @author alex
 *
 */
public interface GrossEarningsProvider extends PaymentProvider, EarningsProvider {

	float getGrossEarnings();

	ReadOnlyProperty<Number> grossEarningsProperty();

}

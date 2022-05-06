package com.example.consumer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * $Id$
 * <p>
 * Created 21/01/2022 by maciasavila
 * </p>
 * <p>
 * [Add comment here]
 * </p>
 * <p>
 * Copyright: Copyright (c) 2020
 * Organisation: Igel Technology GmbH
 * </p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestResponse implements Serializable
{
    String data;
    ResponseStatus status;
}

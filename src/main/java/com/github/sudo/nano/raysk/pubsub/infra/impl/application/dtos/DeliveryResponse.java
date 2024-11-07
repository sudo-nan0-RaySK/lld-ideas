package com.github.sudo.nano.raysk.pubsub.infra.impl.application.dtos;

/**
 * @author Saksham Sethi (saksham.s@media.net)
 * @version 07/11/24
 */
public sealed interface DeliveryResponse permits DeliveryAcknowledgementResponse, DeliveryFailedResponse {}

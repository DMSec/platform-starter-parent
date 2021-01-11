package br.com.dmsec.exception.handler.rest


import br.com.dmsec.exception.handler.rest.error.ErrorMessageResponse
import br.com.dmsec.exception.handler.rest.error.ErrorMessageResponseGenerator
import br.com.dmsec.starter.commons.exception.BusinessDynamicException
import br.com.dmsec.starter.commons.exception.BusinessException
import br.com.dmsec.starter.commons.exception.NotFoundException
import br.com.dmsec.starter.commons.exception.UnauthorizedException
import br.com.dmsec.starter.commons.logger.logger
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.web.bind.annotation.RestControllerAdvice





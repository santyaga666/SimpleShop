<#import "parts/common.ftl" as c>

<@c.page>

<div>
    <div>
        ${point.name}
        ${point.photo}
        ${point.price}
    </div>
    <form method="post" action="cancel">
        <input type="hidden" name="_csrf" value="${_csrf.token}">
        <button type="submit">Отменить</button>
    </form>

    <form method="post" action="checkBalance">
        <input type="hidden" name="_csrf" value="${_csrf.token}">
        <button type="submit">Проверить оплату</button>
    </form>

    <strong>
        ${errorMessage}
    </strong>

    <div>
        <strong>Номер кошелька для оплаты</strong>
        <strong>${wallet}</strong>
    </div>
    <form method="post" action="check">

    </form>

</div>

</@c.page>

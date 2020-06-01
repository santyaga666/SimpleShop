<#import "parts/common.ftl" as c>

<@c.page>

    <div>
    Список пользователей
    <table>
        <thead>
        <tr>
            <th>Name</th>
            <th>Role</th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <#list users as user>
            <tr>
                <td>${user.username}</td>
                <td><#list user.roles as role>${role}<#sep>, </#list></td>
                <td><a href="/user/${user.id}">edit</a></td>
            </tr>
        </#list>
        </tbody>
    </table>
    </div>

    <div>
        Форма добавления нового предмета
        <form method="post">
            <input type="hidden" name="_csrf" value="${_csrf.token}">
            <input type="text" name="photo" placeholder="Добавьте фото">
            <input type="text" name="price" placeholder="Введите цену">
            <input type="text" name="name" placeholder="Введите имя">
            <button type="submit">Добавить</button>
        </form>

        Форма очищения всех репозиториев (Кроме пользовательского). Для разработки
        <form method="post" action="clear">
            <input type="hidden" name="_csrf" value="${_csrf.token}">
            <button type="submit">Очистить</button>
        </form>
    </div>


</@c.page>
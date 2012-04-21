サンプルアプリケーションの解説です。
◆Page
ページクラスの作成方法に関しては、
scenic3sample.page.FrontPage
scenic3sample.page.TwitterPage
を参照してください。
TwitterPageは、Slim3のチュートリアルアプリケーションに対応しています。
<注意事項>
ページクラスを追加した場合には、scenic3sample.controller.AppUrlsにPageMatcherの追加を忘れないようにしてください。

◆AppUrls
AppUrlsのサンプルは、
scenic3sample.controller.AppUrls
を参照してください。

◆Test
PageTestCaseを使う事で、Controllerのテストと同じようにユニットテストを行うことができます。
どのPageクラスのどのアクションメソッドが実行されたかを検証するために、次のメソッドを利用します。
PageTester#getPage
PageTester#getActionMethodName



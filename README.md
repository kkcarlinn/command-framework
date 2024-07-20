
# Command Framework

Uma framework para fácil criação de comandos em bukkit.
Está aqui para uso pessoal, mas você pode usar se quiser :P.

## Uso / Exemplos

1- Adicione a framework no seu projeto, ela está no jitpack, então é só adicionar 

Maven:

```xml
    <repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>


    <dependencies>
        <dependency>
	        <groupId>com.github.skipdevelopment</groupId>
	        <artifactId>command-framework</artifactId>
	        <version>1.0.0</version>
	        </dependency>
    </dependencies>

```

Gradle:

```gradle

    repositories {
		mavenCentral()
		maven { url 'https://jitpack.io' }
	}

	dependencies {
	        implementation 'com.github.skipdevelopment:command-framework:1.0.0'
	}


```


2- Registre o Command Handler: 

```java
import br.com.unidade.engine.command.CommandHandler;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitMain extends JavaPlugin {

    @Getter private static BukkitMain instance;

    public BukkitMain() {
        instance = this;
        CommandHandler.setPlugin(this);
    }

    @Override
    public void onLoad() {
        super.onLoad();
    }
}
```

3- Carregue os comandos pela package.

```java


    @Override
    public void onEnable() {
        CommandHandler.registerCommands("br.com.unidade.example.commands", getInstance()); //No lugar de br.com.unidade.example.commands deve estar a package de onde estão seus comandos!
        super.onEnable();
    }

```

Se ainda tiver dúvidas olhe esse exemplo <a href='https://github.com/skipdevelopment/command-example'>aqui</a>



/*
 * Copyright (c) 2024 Jacob a.k.a Jkaano a.k.a Kaan_Smythe
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package io.github.jkaano.toomanykeybinds.client.config;

import io.github.jkaano.toomanykeybinds.TooManyKeybinds;
import io.github.jkaano.toomanykeybinds.client.TMKPageHandler;
import io.github.jkaano.toomanykeybinds.client.gui.TMKPage;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class ClientConfig {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static TMKPage[] pages = TooManyKeybinds.pageHandler.getPages();

    static List<ForgeConfigSpec.ConfigValue<String>> pageNames;
    static List<ForgeConfigSpec.ConfigValue<Integer>> pageNumbers;

    static {

        BUILDER.push("Too Many Keybinds Configuration");

        generateConfig();

        for(int i = 0; i < pageNames.size()-1; i++){
            pageNames.indexOf(i);
            pageNumbers.indexOf(i);
        }

        BUILDER.pop();
        SPEC = BUILDER.build();

    }

    public static void generateConfig(){

        for(TMKPage page : pages){
            ForgeConfigSpec.ConfigValue<String> name = BUILDER.comment(page.getName()).define(page.getName() + "_name", page.getName());
            ForgeConfigSpec.ConfigValue<Integer> number = BUILDER.define(page.getPageNumber() + "_number", page.getPageNumber());
            pageNames.add(name);
            pageNumbers.add(number);
        }

    }

}

# The QuestKit by rplacd/macinsam

## Licensing due diligence

It would be nice if you credit the QuestKit when using it. I cannot compel you to do so, though - you must, however, abide by the terms of the modified MIT license (I just removed the copyright statement) here:

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in
    all copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
    THE SOFTWARE.

In practical terms, you don't have to do anything other than say QuestKit uses the MIT license if you distribute a significant proportion, whether vanilla more modified, in source or in binary form.

## About this project and the repo

This project adds a quests subsystem to Minecraft - in terms of code, this means "model" classes for a ModLoader mod for top-level keybindings, some GUI classes to handle notifications and the Journal, and modifications to World.java and WorldInfo.java to add the actual quest collection to World, and to get the "model" classes to serialize and deserialize.

It doesn't come with the quests.

You'll need ModLoader, and instructions given here are for Eclipse. If you're not using Eclipse - and I suggest you do, because it works closely with the repo structure - I'll trust you to find your own way of keeping the repo structure intact.

## Before you do anything else:

1. First off, setup your MCP installation - patch your Minecraft jars with ModLoader and decompile them. 

2. Run Eclipse with ./eclipse as your workspace directory, then build and run a vanilla MCP Minecraft as a sanity check.

3. Pull from github to the ./eclipse/Client folder.

## I've pulled from git - now what?

1. Readd the QuestKit folder to the Client project via New > Source Folder.

2. Patch the World.java and WorldInfo.java patches in the diffs folder. Use a GNU patch compatible-program - patch itself is the gold standard. For example, I might first cd into the diffs folder, and issue the following invocations:

    patch ../../../src/minecraft/net/minecraft/src/World.java World.java.diff
	
    patch ../../../src/minecraft/net/minecraft/src/WorldInfo.java WorldInfo.java.diff

3. Sanity test Minecraft again. Generate javadocs from the QuestKit sources if you must.

And then you're pretty much set. Happy hacking.
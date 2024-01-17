local ShapeRenderer = java.import("com.badlogic.gdx.graphics.glutils.ShapeRenderer")
local GL20 = java.import("com.badlogic.gdx.graphics.GL20")
local Gdx = java.import("com.badlogic.gdx.Gdx")

local shapeRenderer = ShapeRenderer.new()

--
--renderer:begin()
--
--print(ShapeRenderer)
--print(renderer)


-- renderer:begin(50)

function render()
    Gdx.gl.glClearColor(1.0, 1.0, 1.0, 1.0)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

    shapeRenderer:Begin()
     --shapeRenderer:begin(ShapeRenderer.ShapeType.Filled)
     --shapeRenderer:setColor(1, 0, 0 ,1)
     --shapeRenderer:rect(2, 20, 100, 100)
    shapeRenderer:End()
end

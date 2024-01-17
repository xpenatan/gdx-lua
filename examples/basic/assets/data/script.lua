ShapeRenderer = java.import("com.badlogic.gdx.graphics.glutils.ShapeRenderer")

renderer = ShapeRenderer.new()

renderer:begin()

print(ShapeRenderer)
print(renderer)


-- renderer:begin(50)

-- function render()
--     Gdx.gl.glClearColor(1.0, 1.0, 1.0, 1.0)
--     Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
--
--     shapeRenderer:begin(ShapeRenderer.ShapeType.Filled)
--     shapeRenderer:setColor(1, 0, 0 ,1)
--     shapeRenderer:rect(2, 20, 100, 100)
--     shapeRenderer:end()
-- end

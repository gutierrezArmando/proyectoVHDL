library ieee;
use ieee.std_logic_1164.all;

entity com_or is
	port (
		a : in std_logic;
		b : in std_logic;
		c : out std_logic
	);
end com_or;

architecture funcional of com_or is
begin
	process ( a, b ) begin
	  if ( a = '0' and b = '0') then
	  	c <= '0' ;
	  else
	  	c <= '1' ;
	  end if ;
	end process ;
end funcional ;